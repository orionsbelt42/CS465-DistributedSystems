package transactionserver;


public class LockType 
{
    private LockName lock;

    public LockType(LockName newlock)
    {
        lock = newlock;
    }

    public void promote()
    {
        switch (lock) {
            case READ_LOCK:
                lock = LockName.WRITE_LOCK;
                break;
            case EMPTY_LOCK:
                lock = LockName.READ_LOCK;
                break;
            case WRITE_LOCK:
                lock = LockName.WRITE_LOCK;
                break;
                
        }
    }

    public void breakLock()
    {
        lock = null;
    }
}
        