import java.util.concurrent.locks;
import java.util.concurrent.locks.ReentrantLock;

public class LockManager
{
	private Hashtable theLocks;

	public void setLock(Object object, TransID trans, LockType lockType)
	{
		Lock foundLock;
		synchronized (this)
		{
			// find lock associated with this object
			// if there isn't one, create it and add to hashtable
		}

		foundLock.acquire(trans, lockType);
	}
	// synchronize this one because we want to remove all entries
    public synchronized void unLock(TransID trans)
	{
		Enumeration enm = theLocks.elements();
		while( enm.hasMoreElements() )
		{
			Lock aLock = (Lock) (e.nextElement());
			if( /*trans is a holder of this lock*/)
			{
				aLock.release(trans);
			}
		}
	}
}
