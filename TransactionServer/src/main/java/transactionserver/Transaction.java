package transactionserver;

import java.util.*;
import utils.SystemLog;

public class Transaction
{
    // transaction id
    int transactionID;
    boolean deadlocked;
    LockType requesting;
    
    private ArrayList<String> transactionRecord; 
    SystemLog serverLog = TransactionServer.log;

    // list of locks
    private static ArrayList<Lock> listOfLocks;

    /**
     * Class Constructor 
     * 
     * @param ID the ID number of the transaction
     */
    public Transaction( int ID )
    {
        // allocate memory for a new list of locks
        listOfLocks = new ArrayList<Lock>();
        
        // default to no deadlocks 
        deadlocked = false;

        // set the transaction id
        transactionID = ID;
        
        transactionRecord = new ArrayList<>(); 
    }

    /**
     * getter function to access transaction ID
     * 
     * @return a copy of the transaction ID
     */
    public int getTID(){
        return transactionID;
    }

    /**
     * getter function to access locks held by the transaction
     * 
     * @return an array list of all locks held by the process
     */
    public ArrayList<Lock> getLockList(){
        return listOfLocks;
    }
    
    /**
     * function to check if two transactions are equal
     * 
     * @param other a transaction object to compare with
     * @return the result of the comparison
     */
    public boolean equals(Transaction other) {
        return this.transactionID == other.transactionID;
    }
    
    public void addLock(Lock newLock){
        listOfLocks.add(newLock);
    }
    
    public void clearLocks(){
        for (Lock lock: listOfLocks) {
            lock.release(this);
        }
    }
    
    public ArrayList<String> getRecord() {
        return transactionRecord;
    }
    
    public void write(String message) {
        transactionRecord.add(message);
        
        serverLog.write(message);
    }
    
    public void setRequesting(LockType requested) {
        requesting = requested;
    }
    
    public LockName getRequesting() {
        return requesting.getLock();
    }
 
    public void signalDeadlock(){
        deadlocked = true;
    }
    public boolean isDeadlocked() {
        return deadlocked;
    }
}