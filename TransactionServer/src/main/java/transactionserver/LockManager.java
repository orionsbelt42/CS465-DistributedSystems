package transactionserver;

import java.util.*;

public class LockManager
{
    private Hashtable<Account, Lock> theLocks = new Hashtable<>();

    public void setLock(Account account, Transaction transaction, LockType lockType)
    {
        Lock foundLock;
        synchronized (this)
        {
            if (!theLocks.containsKey(account))
            {
                foundLock = new Lock(account);
                theLocks.put(account, foundLock);
            }
            else {
                foundLock = theLocks.get(account);
            }
        }

        foundLock.acquire(transaction, lockType);
    }

    // synchronize this one because we want to remove all entries
    public synchronized void unLock(Account account, Transaction transaction)
    {
        Enumeration e = theLocks.elements();
        while (e.hasMoreElements()) {
            Lock aLock = (Lock) (e.nextElement());
            if (aLock.heldBy(transaction)) {
                aLock.release(transaction.getTID());
            }
        }
    }
}
