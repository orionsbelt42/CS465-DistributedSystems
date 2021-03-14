

public class Transaction
{

	public int write( int accountNumber, Transaction transaction )
	{
		Account account = getAccount( accountNumber );
		( TransactionServer.lockManager ).lock( account, transaction, WRITE_LOCK );
		account.setBalance( balance );
		return balance;
	}

	public int read( int accountNumber, Transaction transaction )
	{
		Account account = getAccount( accountNumber );
		( TransactionServer.lockManager ).lock( account, transaction, READ_LOCK );
		return account.getBalance();
	}

	public void openTranscation(account1, account2, money)
	{
		print(" Opening tansaction...")
		Account account = getAccount( account1 );
		Account account = getAccount( account2 );
	}

	public void closeTransaction(transactionNum)
	{
		
	}

}