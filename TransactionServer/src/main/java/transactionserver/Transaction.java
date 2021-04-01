package transactionserver;

import java.util.*;
import utils.SystemLog;

public class Transaction
{
    // transaction id
    public static final int RUNNING = 0;
    public static final int FINISHED = 1;
    public static final int DEADLOCKED = 2;
    
    
    int transactionID;
    int status;
    
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

        // set the transaction id
        transactionID = ID;
        
        // set initial status
        this.status = RUNNING;
        
        // stores all input and output for the transaction
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
    
    /**
     * add lock to list 
     * 
     * @param newLock new lock to add
     */
    public void addLock(Lock newLock){
        listOfLocks.add(newLock);
    }

    /**
     * set current transaction status
     * 
     * @param value new status
     */
    public void setStatus(int value) {
        this.status = value;
    }
    
    /**
     * get transaction value
     * 
     * @return the current status
     */
    public int getStatus() {
        return this.status;
    }
    
    
    /**
     * gets transaction history/record 
     * 
     * @return the record
     */
    public ArrayList<String> getRecord() {
        return transactionRecord;
    }
    
    /**
     * write message to console and file
     * 
     * @param message message to write
     */
    public void write(String message) {
        transactionRecord.add(message);
        
        serverLog.write(message);
    }
    
}