package transactionserver;

        import java.util.*;

public class LockManager
{
    private Hashtable theLocks = new Hashtable();

    public void setLock(Account account, Transaction transaction, LockType lockType)
    {

        Lock foundLock;
        synchronized (this)
        {
            if (!theLocks.contains(account))
            {
                Lock lock = new Lock();
                theLocks.put(account, lock);
            }

            foundLock = theLocks.get(account);


        }

        foundLock.acquire(transaction.getTID(), lockType);
    }

    // synchronize this one because we want to remove all entries
    public synchronized void unLock(Account account, Transaction transaction)
    {
        Lock foundLock = theLocks.get(account);
        foundLock.release(transaction.getTID());

    }
}
