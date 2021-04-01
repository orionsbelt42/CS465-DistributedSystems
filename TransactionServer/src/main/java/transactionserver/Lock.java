package transactionserver;

import java.util.*;
import static transactionserver.LockName.*;
import utils.SystemLog;

public class Lock
{
    private boolean writeConflict = false;
    private final Account acct;
    // transactions that have lock
    private Vector<Transaction> holders;
    private LockType lockType;
    private SystemLog deadlockOut = new SystemLog("deadlock.log");
    
    /**
     * default constructor
     * 
     * @param acct the account to lock
     */
    public Lock( Account acct )
    {
        this.acct = acct; // account held by the lock
        this.holders = new Vector<>();
        this.lockType = new LockType();
    }

    /**
     * Function to acquire account lock for specific transaction
     * 
     * @param transaction the requesting transaction
     * @param newType the desired lock type
     */
    public synchronized void acquire( Transaction transaction, LockType newType) {
        Transaction log = transaction;
        boolean deadlock = false;
        // setup log templates
        String template = "Transaction #" + transaction.getTID() + " [Lock.acquire]                  | ";
        String acctStr = "account #" + acct.getID();
        String currentLock = "current lock " + lockType.toString() + " ";
        
        // notify that lock is wanted
        transaction.write(template + "try " + newType.toString() + " on " + acctStr);
  
        // wait for conflicting locks to resolve
        while (lockConflict(transaction, newType)) {
            try{
                wait();
            } catch( InterruptedException except) {
            }
        }
        
        // if lock is not held
        if (holders.isEmpty()) {
            holders.addElement(transaction); // add transaction to holders
            transaction.addLock(this); // add lock to transaction list
            // print assigned with no conflicts 
            transaction.write(template + currentLock + " on " + acctStr + ", no holder, no conflict");
            lockType = newType; // assign lock type   
            transaction.write(template + "lock set to " + lockType + " on " +acctStr );
            // show lock change
        }
        else if (holders.size() == 1 && holders.contains(transaction)) {
            // print assigned with no conflicts 
            transaction.write(template + "current lock " + lockType.toString() + " on " + acctStr + ", transaction is sole holder, no conflict");
            if (newType.getLock() == WRITE_LOCK) {
                // show lock reassignment
                transaction.write(template + "ignore setting " + lockType.toString() + " to " + newType.toString() + " on" + acctStr);
            }
            else {
                // show as promotion
                transaction.write(template + "promote " + lockType.toString() + " to " + newType.toString() + " on " + acctStr); 
            }
            lockType = newType;// assign lock type 
        }
        else if (lockType.getLock() == READ_LOCK && newType.getLock() == READ_LOCK) {
            // share the lock
            if (!holders.contains(transaction)) {
                holders.addElement(transaction);
                transaction.addLock(this);
            }
            // print output
            transaction.write(template + currentLock + "on " + acctStr + ", sharing lock, no conflict");
            transaction.write(template + "share READ_LOCK on " + acctStr + ", with transaction(s)" + getHoldingStr());
        }
        // conflict reached, must wait untill resolved
        else if ( holders.contains(transaction) && newType.getLock() == WRITE_LOCK ) {
            // log conflict and wait
            transaction.write(template + currentLock + "held by transaction(s)" + getHoldingStr() + " on" + acctStr  + ", new lock " + newType.toString() + ", conflict!");
            transaction.write(template + "---> wait to set WRITE_LOCK on " + acctStr);
            
            // wait till resolved
            if (holders.size() > 1 && lockType.getLock() == READ_LOCK){
                // check for previous conflict (DEADLOCK)
                if (writeConflict) {
                    deadlockOut.write(transaction.getRecord());
                    deadlockOut.close();
                    transaction.setStatus(transaction.DEADLOCKED);
                }
                // signal there is a conflict
                writeConflict = true;
            }
                   
            // wait for other tranactions to finish
            while (holders.size() > 1) {
                try{
                    wait();
                } catch( InterruptedException except) {
                }
            }
            writeConflict = false; // assumed resolved and remove conflict flag
            
        }
    }
    
    
    /**
     * generates a string containing all holding transactions
     * 
     * @return the list of all transactions
     */
    public String getHoldingStr() {
        String buffer = "";
        for (Transaction current: holders) {
            buffer += " " + current.getTID();
        }
        return buffer;
    }

    
    /**
     * checks for conflicting locks
     * 
     * @param current current transaction
     * @param newType new lock type
     * @return if there is a conflict
     */
    public boolean lockConflict(Transaction current, LockType newType) {
        // if the transaction already holds the lock
        if (holders.contains(current)) {
            return false;
        }
        // read + read so share
        else if (lockType.getLock() == READ_LOCK && newType.getLock() == READ_LOCK) {
            return false;
        }
        // empty lock means go ahead
        else if (lockType.getLock() == EMPTY_LOCK) {
            return false;
        }
        // there is a conflict
        else { return true; }
    }
    
    
    /**
     * release transaction locks
     * 
     * @param transaction working transaction
     */
    public synchronized void release(Transaction transaction)
    {
        // remove the transaction
        holders.removeElement(transaction);
        if (holders.isEmpty())
        {
            // if it is clear reset to empty lock
            lockType.clearLock();
        }
        // signal waiting threads
        notifyAll();
    }
    
    
    /**
     * get account id number
     * 
     * @return id number
     */
    public int getAcctID() {
        return acct.getID();
    }
    
    
    /**
     * get the current lock as a string
     * 
     * @return the lock string
     */
    public String currentLockType() {
        return lockType.toString();
    }
    
    
    /**
     * checks if a given transaction holds the lock
     * 
     * @param transaction transaction to check
     * @return the result
     */
    public boolean heldBy(Transaction transaction) {
        return holders.contains(transaction);
    }
}
