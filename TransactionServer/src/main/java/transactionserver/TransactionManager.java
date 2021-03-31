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
        AccountManager acctManager;

        SystemLog log = TransactionServer.log;
        public TransactionManagerWorker(Socket socket, AccountManager acctManager, int id) {
            
            try {
                this.connection = socket;
            
                this.fromClient = connection.getInputStream();
                this.toClient = connection.getOutputStream();
            } catch (IOException e) {
                System.out.println("Error getting connection to client");
            }

            transaction = new Transaction(id);
            reader = new MessageReader(true);
            this.acctManager = acctManager;
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
            int transID = -1;
            int acctNum;
            int amount;
            int balance;
            
            boolean keepOpen = true;
            ArrayList<String> args;
            
            if (msg.equals("OPEN_TRANSACTION: null")) {
                
                // create transaction object
                transactionList.add(transaction);
                writer = new MessageWriter(transaction.getTID());
                log.write("Transaction #" + transaction.getTID() + " [TransactionManagerWorker.run] OPEN_TRANSACTION #" + transaction.getTID());
                send(writer.transactionResponse(accounts));
                
            }
            
            while (keepOpen)
            {
                msg = recv();
                args = reader.parseMessage(msg);
                
                switch(args.get(0)) {
                    case "READ_REQUEST":
                        transID = Integer.parseInt(args.get(1));
                        acctNum = Integer.parseInt(args.get(2));
                        log.write("Transaction #" + transID + " [TransactionManagerWorker.run] READ_REQUEST  >>>>>>>>>>>>>>>>>>>> account #" + acctNum);
                        balance = acctManager.read(acctNum, transaction);
                        log.write("Transaction #" + transID + " [TransactionManagerWorker.run] READ_REQUEST  <<<<<<<<<<<<<<<<<<<< account #" + acctNum + ", balance $" + balance);
                        send(writer.readResponse(acctNum, balance));
                        break;
                    case "WRITE_REQUEST":
                        transID = Integer.parseInt(args.get(1));
                        acctNum = Integer.parseInt(args.get(2));
                        amount = Integer.parseInt(args.get(3));
                        log.write("Transaction #" + transID + " [TransactionManagerWorker.run] WRITE_REQUEST >>>>>>>>>>>>>>>>>>>> account #" + acctNum + ", new balance $" + amount);
                        balance = acctManager.write(acctNum, transaction, amount);
                        log.write("Transaction #" + transID + " [TransactionManagerWorker.run] WRITE_REQUEST <<<<<<<<<<<<<<<<<<<< account #" + acctNum + ", new balance $" + balance);
                        send(writer.writeResponse(acctNum, balance));
                        break;
                    case "CLOSE_TRANSACTION":
                        
                        log.write("Transaction #" + transID + " [TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transID);
                        keepOpen = false;
                }
                
                
            }
            transaction.clearLocks();
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