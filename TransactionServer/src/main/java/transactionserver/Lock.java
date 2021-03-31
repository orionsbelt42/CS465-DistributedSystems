package transactionserver;

import java.util.*;
import transactionclient.TransactionClient;
import static transactionserver.LockName.*;
import utils.SystemLog;

public class Lock
{
    private Account acct;
    // transactions that have lock
    private Vector<Transaction> holders;
    private Vector<Transaction> requesting;
    private LockType lockedType;
    
    

    public Lock( Account acct )
    {
        this.acct = acct;
        this.holders = new Vector<>();
        this.requesting = new Vector<>();
        this.lockedType = new LockType();
    }

    
    public synchronized void acquire( Transaction transaction, LockType lockType) {
        Transaction log = transaction;
        boolean deadlock = false;
        
        String template = "Transaction #" + transaction.getTID() + " [Lock.acquire]                  | ";
        String accountStr = "account #" + acct.getID();
        boolean doneWaiting = false;
        boolean waitRecorded = false;
        LockType temp = lockedType;
        log.write(template + "try " + lockedType.toString() + " on " + accountStr);
        while (checkForConflicts(transaction, lockedType, lockType)) {
            try {
                if (!waitRecorded) {
                    log.write(template + "current lock " + lockedType.toString() + " held by transaction(s)" + getHolderStr(transaction) + " on " + accountStr + ", new lock " + lockType.toString() + ", conflict!");
                    log.write(template + "---> wait to set WRITE_LOCK on " + accountStr);
                    requesting.add(transaction);
                    transaction.addLock(this);
                    waitRecorded = true;
                    transaction.setRequesting(lockType);
                }
                wait();
                
                doneWaiting = true;
            } catch (InterruptedException e) {
            }
        }
        
        if (doneWaiting){
            System.out.println("GOT OUT");
            requesting.remove(transaction);       
        }
        
        // "Transaction #" + transaction.getTID() + " [Lock.acquire]                 | lock set to " + lockedType.toString() + " on account #" + acct.getID()
        // Transaction #" + transaction.getTID() + " [Lock.acquire]                 | current lock " + lockedType.toString() + " on account #" + acct.getID() + ", no holder, no conflict");
        if (holders.isEmpty()) {
            holders.addElement(transaction);
            log.write(template + "current lock " + lockedType.toString() + " on " + accountStr + ", no holder, no conflict");
            lockedType = lockType;
            log.write(template + "lock set to " + lockedType.toString() + " on " + accountStr);
            
        }
        else if (holders.size() == 1 && holders.get(0).equals(transaction)) {
             
            log.write(template + "current lock " + lockedType.toString() + " on " + accountStr + ", transaction is sole holder, no conflict");
            log.write(template + "ignore setting " + lockedType.toString() + " to " + lockType.toString() + " on " + accountStr);
            lockedType = lockType;
        }
        else if (lockedType.getLock() == READ_LOCK && lockType.getLock() == READ_LOCK) {
            if (!holders.contains(transaction)) {
                holders.addElement(transaction);
            }
            log.write(template + "current lock " + lockedType.toString() + " on " + accountStr + ", sharing lock, no conflict");
            transaction.addLock(this);
            
  
        }
        else if (holders.contains(transaction)) {
             
            while (holders.size() < 1 && !deadlock)
            {
                try {
                    wait();
                    
                    deadlock = checkForDeadlocks(transaction);
                } catch (InterruptedException e) {
                }
            }
            
            if (!deadlock) {
                lockType.promote();
                log.write(template + "promote " + temp.toString() + " to " + lockedType.toString() + " on account #" + acct.getID());
            }
        }
    }
    
   
    private boolean checkForConflicts(Transaction transaction, LockType lock1, LockType lock2) {
        if (holders.contains(transaction)){
            return false;
        }
        else if (lock1.getLock() == READ_LOCK && lock2.getLock() == WRITE_LOCK)
        {
            return true;
        }
        
        else if (lock2.getLock() == READ_LOCK && lock1.getLock() == WRITE_LOCK)
        {
            return true;
        }
        
        else if (lock2.getLock() == WRITE_LOCK && lock1.getLock() == WRITE_LOCK)
        {
            return true;
        }
        
        return false;
    }
    
    private boolean checkForDeadlocks(Transaction current) {
        int count = 0;
        System.out.println("DEADLOCK");
        if (current.getRequesting() == WRITE_LOCK) {
            for (Transaction holding: holders){
                if (holding.getRequesting() == WRITE_LOCK) {
                    count += 1;
                }
            }
            
            if (count > 1) {
                current.signalDeadlock();
                return true;
            }
        }
        
        return false;
    }
    
    public String getHolderStr(Transaction current) {
        String holding = "";
        
        for (Transaction item: holders) {
            if (!item.equals(current)) {
                holding += " " + item.getTID();
            }
        }
        
        return holding;
    }
    
    public LockName getLockType() {
        return lockedType.getLock();
    }
    
    /*
        if (lockType == READ_LOCK)
        {
            if ((lockedType == READ_LOCK) || (lockedType == EMPTY_LOCK))
            {
                holders.addElement(transid);
                lockedType = READ_LOCK;
            }
        }
        else if (lockType == WRITE_LOCK)
        {
            if (lockedType == EMPTY_LOCK)
            {
                holders.addElement(transid);
                lockedType = WRITE_LOCK;
            }
            else
            {
                // Wait for other locks to clear
                while (!holders.isEmpty())
                {
                    try{
                        wait();
                    } catch( InterruptedException except) {
                    }
                }
                holders.addElement(transid);
                lockedType = WRITE_LOCK;
            }
        }
    */
        //while(/*another transaction holds lock in conflicting mode*/) {
        //    try{
        //        wait();
        //    } catch( InterruptedException except) {
        //    }
        //}

        //if( holders.isEmpty() ) // no TIDs hold lock
        //{
        //    holders.addElement(trans);
        //    lockType = lockType;
        //}
        //else if ( /*another transaction holds the lock, share it*/)
        //{
        //    if(/*This transaction not a holder*/)
        //    {
        //        holders.addElement(trans);
        //    }
        //}
        //else if (/*this transaction is a holder but needs more exclusive lock*/)
        //{
        //    lockType.promote();
        //}

    public synchronized void release(Transaction transaction)
    {

        holders.removeElement(transaction);
        if (holders.isEmpty())
        {
            lockedType.clearLock();
        }
        notifyAll();
    }
    
    public boolean heldBy(Transaction transaction) {
        return holders.contains(transaction);
    }
}
