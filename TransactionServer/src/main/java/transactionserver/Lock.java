package transactionserver;

import java.util.*;

public class Lock 
{
    private Object object;
    // transactions that have lock
    private Vector holders;
    private LockType lockType;

    public synchronized void acquire( TransID trans, LockType aLockType){
        while(/*another transaction holds lock in conflicting mode*/) {
            try{
                wait();
            } catch( InterruptedException except) {
            }
        }

        if( holders.isEmpty() ) // no TIDs hold lock
        { 
            holders.addElement(trans);
            lockType = aLockType;
        } 
        else if ( /*another transaction holds the lock, share it*/)
        {
            if(/*This transaction not a holder*/)
            {
                holders.addElement(trans);
            }
        }
        else if (/*this transaction is a holder but needs more exclusive lock*/)
        {
            lockType.promote();	
        }
    }

    public synchronized void release(TransID trans)
    {
        holders.removeElement(trans);
        //set lockType to none
        notifyAll();
    }
}