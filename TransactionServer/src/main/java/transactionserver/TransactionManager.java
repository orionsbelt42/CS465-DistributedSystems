package transactionserver;

import java.net.*;
import java.util.*;
import java.io.*;
import utils.*;

public class TransactionManager
{
    static ArrayList<Transaction> transactionList; // array for all transactions
    
    public class TransactionManagerWorker implements Runnable {
        
        Socket connection; // socket connection to client
        InputStream fromClient; // input stream from client
        OutputStream toClient; // output stream to client
        
        Transaction transaction; // current working transaction

        MessageReader reader; // standardized message reader
        MessageWriter writer; // standardized message reader

        ArrayList<Account> accounts; // account array to send back to client

        public TransactionManagerWorker(Socket socket, AccountManager acctManager) {
            
            try {
                this.connection = socket;
            
                this.fromClient = connection.getInputStream();
                this.toClient = connection.getOutputStream();
            } catch (IOException e) {
                System.out.println("Error getting connection to client");
            }

            reader = new MessageReader();

            this.accounts = acctManager.getAccounts();

        }
        
        /**
         * finds the next available id for a new transaction
         * 
         * @param transactions a list of transaction
         * @return the next unassigned ID
         */
        private int getNextID(ArrayList<Transaction> transactions) {
            int max = 0; // local max
            
            // loop through transactions in the list
            for (Transaction trans: transactions) {
                // check if current transaction id is larger than local max
                if ( max < trans.getTID() ) { 
                    max = trans.getTID() + 1; // larger value found, increment 
                }
            }
            
            return max; // return the result
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
                System.out.println("Couldn't send message to Client");
            }
        }
        
        /**
         * Listen for any messages from client
         * 
         * @return a string containing a line of text from the client
         */
        private String recv() {
            String buffer = ""; // buffer for client byte storage
            char current; // holder for current char from client
            try {
                current = (char)fromClient.read(); // get first char
                
                // loop till end of line
                while (current != '\n') {
                    buffer += current; // add char to the buffer
                    current = (char)fromClient.read(); // get the next char
                }
            } catch (IOException e) {
                System.out.println("Error while reading from socket");
            }
            
            return buffer; // return message from client
        }

        @Override
        public void run() {
            // recv and verify first message
            String msg = recv();
            int transID;
            ArrayList<String> args;
            
            if (msg.equals("OPEN_TRANSACTION: null")) {
                
                // create transaction object
                transID = getNextID(transactionList);

                transaction = new Transaction(transID);
                transactionList.add(transaction);
                writer = new MessageWriter(transaction.getTID());
                send(writer.transactionResponse(accounts));
            }
            
            while (!msg.equals("CLOSE_TRANSACTION: null"))
            {
                String msg = recv();
                args = reader.parseMessage(msg);
                
                switch(args[0]) {
                    case "READ_REQUEST":
                        
                        send(writer.readResponse(int accountID, int balance))
                }
            }
            
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
}