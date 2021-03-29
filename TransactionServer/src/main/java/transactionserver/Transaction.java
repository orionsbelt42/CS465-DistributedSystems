package transactionserver;

import java.util.*;

public class Transaction
{
    // transaction id
    int transactionID;

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
   
}