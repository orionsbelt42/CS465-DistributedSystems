package transactionserver;

import static transactionserver.LockName.*;

public class LockType 
{
    private LockName lock;

    public LockType()
    {
        lock = LockName.EMPTY_LOCK;
    }
    
    public LockType(LockName newlock)
    {
        lock = newlock;
    }

    /**
     * function to promote lock
     */
    public void promote()
    {
        switch (lock) {
            case READ_LOCK:
                lock = WRITE_LOCK;
                break;
            case EMPTY_LOCK:
                lock = READ_LOCK;
                break;
            case WRITE_LOCK:
                lock = WRITE_LOCK;
                break;
                
        }
    }

    public void clearLock()
    {
        lock = LockName.EMPTY_LOCK;
    }
    
    public LockName getLock() {
        return lock;
    }
    
    @Override
    public String toString() {
        String lockStr = "EMPTY_LOCK";
        switch (lock) {
            case EMPTY_LOCK:
                lockStr = "EMPTY_LOCK";
                break;
            case READ_LOCK:
                lockStr = "READ_LOCK";
                break;
            case WRITE_LOCK:
                lockStr = "WRITE_LOCK";
                break;
        }
        
        return lockStr;
    }
}
        