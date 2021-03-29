package transactionserver;

import java.util.*;

public class Lock
{
    //private Account object;
    // transactions that have lock
    private Vector holders;
    private LockType lockedType;

    //public Lock( Account acct ){
    public Lock()
    {
        holders = new Vector();
    }


//    public Account getObject() {
 //       return object;
  //  }

    public synchronized void acquire( int transid, LockType lockType){
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
    }

    public synchronized void release(int transid)
    {
        holders.removeElement(transid);
        if (holders.isEmpty())
        {
            lockedType = EMPTY_LOCK;
        }
        notifyAll();
    }
}
