class Account
{
	private int ID;
	private int accountTotal;

	public Account(int idNum, int moneyInAccount)
	{
		ID = idNum;
		accountTotal = moneyInAccount)
	}

	public int getTotal()
	{
		return accountTotal;
	}

	public void withdraw( int amount )
	{
		accountTotal -= amount;
	}

	public void deposit( int amount )
	{
		accountTotal += amount
	}
}
