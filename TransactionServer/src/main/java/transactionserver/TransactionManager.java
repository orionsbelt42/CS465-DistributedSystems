package transactionserver;

import java.net.*;
import java.util.*;
import java.io.*;
import utils.*;

public class TransactionManager
{
    static ArrayList<Transaction> transactionList;
    
    
    public class TransactionManagerWorker implements Runnable {
        
        Socket connection;
        InputStream fromClient;
        OutputStream toClient;
        Transaction transaction;
        int ID;

        MessageReader reader;
        MessageWriter writer;

        ArrayList<Accounts> accounts;

 

        public TransactionManagerWorker(Socket socket, AccountManager acctManager) {
            this.connection = socket;
            this.fromClient = connection.getInputStream();
            this.toClient = connection.getOutputStream();

            
            this.ID = transaction.getTID();
            TransactionManager.lastID = this.ID;

            reader = new MessageReader();
            writer = new MessageWriter(ID);

            this.accounts = acctManager.getAccounts();

        }
        
        /**
         * primitive function to send simple text to a server
         * (might change)
         * 
         * @param MSG utf-8 encoded string to send
         */
        private void send(byte[] MSG) {
            try {
                // convert message to bytes and send to server
                this.toClient.write(MSG);
            } catch (IOException e) {
                System.err.println("Couldn't send message to Client");
            }
            
        }
        
        
        private String recv() {
            String buffer = "";
            char current;
            try {
                current = (char)fromClient.read();
                while (current != '\n') {
                    buffer += current;
                    current = (char)fromClient.read();
                }
            } catch (IOException e) {
                System.out.println("Error while reading from socket");
            }
            
            return buffer;
        }

        @Override
        public void run() {
            // recv and verify first message
            String msg = recv();

            if (msg.equals("OPEN_TRANSACTION: null")) {
                send(writer.transactionResponse(accounts));
            }


            // create transaction object
            transaction = new Transaction(ID, logInfo)
            // assign id to transaction object
            
            // add transaction to list of transactions held by manager
            
            // return transaction id to clientAPI 
            
            // loop over next messages 
                // process message (big switch statement)
                
                // handle read/write requests through AccountManager
            
        }
        
    }
    
    public TransactionManager(String logFile) {
        this.transactionList = new ArrayList<Transaction>();

    }
    
//        public int write( int accountNumber, Transaction transaction )
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
    // not sure what else it does

}