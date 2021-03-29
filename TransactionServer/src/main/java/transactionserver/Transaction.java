package transactionserver;
import java.util.*;

public class Transaction
{
   // transaction id
   int transactionID;

   // list of locks
   private static ArrayList<Lock> listOfLocks;

   // logging information
   int loggingInfo;

    /**
     * Class Constructor 
     * 
     * @param ID the ID number of the transaction
     * @param logInfo the logging information of the transaction
     */
   public Transaction( int ID, int logInfo)
   {
      // allocate memory for a new list of locks
      listOfLocks = new ArrayList<Lock>();

      // set the transaction id
      transactionID = ID;
      
      // set the logging
	   loggingInfo = logInfo;
   }

   public int getTID(){
      return transactionID;
   }

   public ArrayList<Lock> getLockList(){
      return listOfLocks;
   }
}