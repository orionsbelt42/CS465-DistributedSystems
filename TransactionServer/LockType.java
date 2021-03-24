public enum lockname{ READ_LOCK, WRITE_LOCK, EMPTY_LOCK }

public class LockType 
{
    private lockname lock;

    public LockType(lockname newlock)
    {
        newlock;
    }

    public promote()
    {
        if( lock == READ_LOCK )
        {
            lock = WRITE_LOCK;
        }
        else if( lock == EMPTY_LOCK )
        {
            lock = READ_LOCK;
        }
        else // WRITE_LOCK cannot be promoted; redefine as self
        {
            lock = WRITE_LOCK;
        }
    }

    public void breakLock()
    {
        lock = NULL;
    }
}