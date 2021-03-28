package transactionserver;
import java.util.*;

public class Transaction
{
   // transaction id
   int transactionID;

   // list of locks
   ArrayList listOfLocks;

   // logging information
   int loggingInfo;

   // create instantiation 
   public Transaction( int ID, ArrayList list, int logInfo)
   {
	   transactionID = ID;
	   listOfLocks = list;
	   loggingInfo = logInfo;
   }

//    public int write( int accountNumber, Transaction transaction )
//    {
//        Account account = getAccount( accountNumber );
//        ( TransactionServer.lockManager ).lock( account, transaction, WRITE_LOCK );
//        account.setBalance( balance );
//        return balance;
//    }
//
//    public int read( int accountNumber, Transaction transaction )
//    {
//        Account account = getAccount( accountNumber );
//        ( TransactionServer.lockManager ).lock( account, transaction, READ_LOCK );
//        return account.getBalance();
//    }
//
//    public void openTranscation(account1, account2, money)
//    {
//        print(" Opening tansaction...")
//        Account account = getAccount( account1 );
//        Account account = getAccount( account2 );
//    }
//
//    public void closeTransaction(transactionNum)
//    {
//
//    }
}