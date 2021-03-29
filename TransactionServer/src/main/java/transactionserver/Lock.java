package transactionserver;

import java.util.*;
import static transactionserver.LockName.*;

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
        
        boolean doneWaiting = false;
        
        while (checkForConflicts(lockedType, lockType)) {
            try {
                requesting.add(transaction);
                wait();
                
                doneWaiting = true;
            } catch (InterruptedException e) {
            }
        }
        
        if (doneWaiting){
            requesting.remove(transaction);       
        }
        
        if (holders.isEmpty()) {
            holders.addElement(transaction);
            lockedType = lockType;
        }
        else if (lockedType.getLock() == READ_LOCK && lockType.getLock() == READ_LOCK) {
            if (!holders.contains(transaction)) {
                holders.addElement(transaction);
            }
        }
        else if (holders.contains(transaction)) {
            while (lockedType.getLock() != EMPTY_LOCK )
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
       
            lockType.promote();
        }
        
    }
    
    private boolean checkForConflicts(LockType lock1, LockType lock2) {
        if (lock1.getLock() == READ_LOCK && lock2.getLock() == WRITE_LOCK)
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

    public synchronized void release(int transid)
    {
        holders.removeElement(transid);
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
