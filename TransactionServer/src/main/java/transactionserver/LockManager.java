package transactionserver;

import java.util.*;

public class LockManager
{
    private Hashtable<Account, Lock> theLocks = new Hashtable<>(); // list of all locks
    
    /**
     * try to set lock for transaction
     * 
     * @param account account to lock
     * @param transaction transaction requesting the lock
     * @param lockType lock type requested
     */
    public void setLock(Account account, Transaction transaction, LockType lockType)
    {
        Lock foundLock;
        synchronized (this)
        { 
            // create new lock if none exists
            if (!theLocks.containsKey(account))
            {
                foundLock = new Lock(account);
                theLocks.put(account, foundLock);
                // log operation
                transaction.write("Transaction #" + transaction.getTID() + " [LockManager.setLock]           | lock created, account #" + account.getID());
            }
            // get existing lock object
            else {
                foundLock = theLocks.get(account);
            }
        }
        // send to function to the aquire the lock for transaction
        foundLock.acquire(transaction, lockType);
    }

    /**
     * unlock all locks held by a transaction
     * 
     * @param transaction transaction to free
     */
    public synchronized void unLock(Transaction transaction)
    {
        // synchronize this one because we want to remove all entries
        String template = "Transaction #" + transaction.getTID() + " [LockManager.unLock]            | ";
        // loop through all locks 
        Enumeration e = theLocks.elements();
        while (e.hasMoreElements()) {
            Lock aLock = (Lock) (e.nextElement());
            // if the lock is held
            if (aLock.heldBy(transaction)) {
                aLock.release(transaction); // release the lock
                transaction.write(template + "release " + aLock.currentLockType() + ", account #" + aLock.getAcctID());
            }
        }
    }
}
