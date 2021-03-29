package transactionserver;

import java.util.*;

// this one still needs a lot of work
public class LockManager
{
    private Hashtable theLocks;

    public void setLock(Account object, Transaction transaction, LockType lockType)
    {
        Transaction elem;
        ArrayList<Lock> locks;

        Lock foundLock;
        synchronized (this)
        {
            Enumeration enm = theLocks.elements();

            while(enm.hasMoreElements()){
                elem = enm.nextElement();
                
                // WHY DOES THE TRANSACTION CLASS HOLD THE LOCKS WHEN WE ARE CHECKING IF SPECIFIC ACCOUNTS
                // ARE LOCKED........ OUR SETUP NOW PREVENTS INDIVIDUAL ACCOUNTS FROM HOLDING LOCKS

                // WE MAY NEED TO REFACTOR, OR ELSE CHANGE HOW WE VIEW HOW LOCKS WORK - IF A TRANSACTION LOCKS
                // ALL ACCOUNTS IT MANAGES WITH THE SAME SINGULAR LOCK IT HOLDS......

                locks = elem.getLockList()

            }

            // find lock associated with this object
            // if there isn't one, create it and add to hashtable
        }

        foundLock.acquire(transaction, lockType);
    }
    
    // synchronize this one because we want to remove all entries
    public synchronized void unLock(Transaction transaction)
    {
        Enumeration enm = theLocks.elements();
        while( enm.hasMoreElements() )
        {
            Lock aLock = (Lock) (e.nextElement());
            if( /*trans is a holder of this lock*/)
            {
                aLock.release(transaction);
            }
        }
    }
}
