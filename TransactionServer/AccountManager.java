private Account[] accountList;

	public AccountManager( int numOfAccounts, int valueInAccounts )
	{
		if (numOfAccounts <= 0)
		{	
			print("the number of accounts must be greater than zero");
		}
		else 
		{
			accountList = new Account[numOfAccounts];
			id = 1;
			while( id <= numOfAccounts )
			{
				accountList[id] = new Account( id, valueInAccounts );
				id += 1;
			}
		}
	}

	public void getAccountValue( int accountID )
	{
		// implement lock using LockManager -- done through AccountManager

		//
		print("Account " + accountID + " contains $" + accountList[accountID].getTotal() );
		// remove lock using LockManager -- Must be done through TransactionManager

		//
	}

	public void 
}
