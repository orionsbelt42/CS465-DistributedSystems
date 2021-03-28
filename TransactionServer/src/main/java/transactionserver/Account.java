package transactionserver;

public class Account
{
    private int ID;
    private int accountTotal;

    public Account(int idNum, int moneyInAccount)
    {
	ID = idNum;
	accountTotal = moneyInAccount;
    }

    public void withdraw( int amount )
    {
	accountTotal -= amount;
    }

    public void deposit( int amount )
    {
	accountTotal += amount;
    }
        
    public void setBalance(int amount) {
        accountTotal += amount;
    }
    
    public int getBalance() {
        return accountTotal;
    }
    
    public int getID() {
        return ID;
    }
}
