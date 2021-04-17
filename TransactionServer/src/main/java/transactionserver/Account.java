package transactionserver;

public class Account
{
    private int ID;
    private int accountTotal;
    private boolean involvedInDeadlock;

    /**
     * account object constructor class
     * 
     * @param idNum unique id for this account
     * @param moneyInAccount integer amount to store at account creation
     */
    public Account(int idNum, int moneyInAccount)
    {
	ID = idNum;
	accountTotal = moneyInAccount;
        involvedInDeadlock = false;
    }

    /**
     * easy withdraw method
     * 
     * @param amount integer amount to withdraw
     */
    public void withdraw( int amount )
    {
	accountTotal -= amount;
    }

    /**
     * easy deposit method 
     * 
     * @param amount integer amount to deposit
     */
    public void deposit( int amount )
    {
	accountTotal += amount;
    }
        
    /**
     * setter method for account balance
     * 
     * @param amount int amount to set as balance
     */
    public void setBalance(int amount) {
        accountTotal += amount;
    }
    
    /**
     * getter method for account balance
     * 
     * @return current account balance
     */
    public int getBalance() {
        return accountTotal;
    }
    
    /**
     * getter method for account ID
     * 
     * @return current account ID
     */
    public int getID() {
        return ID;
    }
    
    /**
     * function to check if two accounts are equal
     * 
     * @param other account to compare with
     * @return the boolean result of the comparison
     */
    public boolean equals(Account other) {
        return (this.ID == other.ID);
    }
    
    /**
     * checks if the account was involved in a deadlock
     * 
     * @return if the account was involved in a deadlock
     */
    public boolean deadlocked() {
        return involvedInDeadlock;
    }
    
    
    /**
     * signals account had deadlocking transactions
     */
    public void setDeadlockFlag() {
        involvedInDeadlock = true;
    }
}
