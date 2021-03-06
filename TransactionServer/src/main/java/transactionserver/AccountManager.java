package transactionserver;
import static transactionserver.LockName.*;
import java.util.ArrayList;


public class AccountManager
{
    private static ArrayList<Account> accounts; // holds all account objects
    private static int numberOfAccounts; // total number of accounts
    private static int initialBalance; // the initial balance for each account

    private boolean lockTransactions;
    public static final int DEADLOCK = -99999999;
    /**
     * Class Constructor 
     * 
     * @param numAccounts the number of accounts to create
     * @param initialBalance the initial balance each account starts with
     */
    public AccountManager( int numAccounts, int initialBalance, boolean applyLocking )
    {
        // allocate memory for arrayList
        accounts = new ArrayList<Account>();
        // set the number of accounts
        AccountManager.numberOfAccounts = numAccounts;
        // set the initial balance for all the accounts
        AccountManager.initialBalance = initialBalance;
        
        // set locking
        lockTransactions = applyLocking;
        // account counter, holds the total number of created accounts 
        int acctIdx;
        
        // loop till all accounts are created
        for ( acctIdx = 0; acctIdx < numAccounts; acctIdx++ ) {
            // create an account and try to insert it at the index = account id
            accounts.add(acctIdx, new Account(acctIdx, initialBalance));
        }
    }
    
    /**
     * gets the account associated with the supplied id 
     *
     * @param accountID the id and index of the desired account
     * @return an account object from the account list
     */
    public Account getAccount( int accountID )
    {
        // return the account at accountID index 
        return accounts.get(accountID);
    }

    /**
     * A function to return all stored account objects
     * 
     * @return all accounts in the list
     */            
    public ArrayList<Account> getAccounts()
    {
        // return the full account arraylist
        return accounts;
    }

    /**
     * Upper level function to write value to an account 
     * 
     * @param accountNumber the id number for the account to write to
     * @param transaction a transaction that wants to edit the account
     * @param balance the new balance
     * @return the set new balance
     */
    public int write( int accountNumber, Transaction transaction, int balance )
    {
        // get the account matching the id
        Account account = getAccount( accountNumber );
           
        // if locking is enabled lock
        if (lockTransactions){
            // create variable for lock type
            LockType lockType = new LockType(WRITE_LOCK);
            
            // lock the account for writing so no other transactions can read/write it 
            ( TransactionServer.lockManager ).setLock( account, transaction, lockType );
        }
        
        
        if (transaction.getStatus() == Transaction.DEADLOCKED) {
            return 0;
        }
        
        // with lock set, update the account balance
        account.setBalance( balance );
        
        // return the updated balance
        return balance;
    }

    /**
     * Upper level function to read a value from an account
     * 
     * @param accountNumber account id for the account to read from
     * @param transaction the transaction requesting the read
     * @return the data read from the account
     */
    public int read( int accountNumber, Transaction transaction )
    {
        // get the account matching the id 
        Account account = getAccount( accountNumber );
        
        
        if (lockTransactions){
            
            // create variable for lock type
            LockType lockType = new LockType(READ_LOCK);
            // if locking is enabled lock
            
            // lock the account for reading 
            ( TransactionServer.lockManager ).setLock( account, transaction, lockType);
        }
        
        // after locking, read and return account balance 
        return (getAccount( accountNumber )).getBalance();
    }
    
    /**
     * gets total sum of all managed accounts
     * 
     * @return 
     */
    public int getBranchTotal() {
        int total = 0;
        for (Account current: accounts) {
            total += current.getBalance();
        }
        
        return total;
    }
}
