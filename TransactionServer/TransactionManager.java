public ArrayList<Account> getAccounts(){
    return accounts;
        }
public int write( int accountNumber, Transaction transaction ){
        Account account = getAccount( accountNumber );
        ( TransactionServer.lockManager ).lock( account, transaction, WRITE_LOCK );
        account.setBalance( balance );
        return balance;
        }
public int read( int accountNumber, Transaction transaction ){
        Account account = getAccount( accountNumber );
        ( TransactionServer.lockManager ).lock( account, transaction, READ_LOCK );
        return account.getBalance();
        }