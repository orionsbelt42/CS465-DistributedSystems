package transactionserver;

import java.net.*;
import java.util.*;
import java.io.*;
import utils.*;

public class TransactionManager
{
    static ArrayList<Transaction> transactionList; // array for all transactions
    static ArrayList<Transaction> deadlockedList; // array for all deadlocked transactions
    
    /**
     * Nested worker class
     */
    public class TransactionManagerWorker implements Runnable {
        
        Socket connection; // socket connection to client
        ServerSocket server;
        InputStream fromClient; // input stream from client
        OutputStream toClient; // output stream to client
        
        Transaction transaction; // current working transaction

        MessageReader reader; // standardized message reader
        MessageWriter writer; // standardized message reader

        ArrayList<Account> accounts; // account array to send back to client
        AccountManager acctManager;

        SystemLog log = TransactionServer.log;
        
        /**
         * Default constructor
         * 
         * @param socket socket for connection
         * @param acctManager program account manager option
         * @param id id number to assign to transaction
         */
        public TransactionManagerWorker(ServerSocket sSocket, Socket socket, AccountManager acctManager, int id) {
            
            try {
                this.connection = socket;
                this.server = sSocket;
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

        /**
         * thread run method
         */
        @Override
        public void run() {
            // recv and verify first message
            String msg = recv();
            int transID = -1; // 
            int acctNum;
            int amount;
            int balance;
            
            boolean keepOpen = true;
            ArrayList<String> args;
            
            if (msg.equals("OPEN_TRANSACTION: null")) {
                
                // create transaction object and add to list
                transactionList.add(transaction);
                writer = new MessageWriter(transaction.getTID()); // create new message creator
                transaction.write("Transaction #" + transaction.getTID() + " [TransactionManagerWorker.run] OPEN_TRANSACTION #" + transaction.getTID());
                send(writer.transactionResponse(accounts)); // send respon
                transaction.setStatus(transaction.RUNNING); // set transaction status
                
            }
            
            while (keepOpen)
            {
                msg = recv();
                args = reader.parseMessage(msg); // extract values from message
                
                switch(args.get(0)) {
                    case "READ_REQUEST":
                        // convert message specfic values to correct types
                        transID = Integer.parseInt(args.get(1));
                        acctNum = Integer.parseInt(args.get(2)); 
                        transaction.write("Transaction #" + transID + " [TransactionManagerWorker.run] READ_REQUEST  >>>>>>>>>>>>>>>>>>>> account #" + acctNum);
    
                        balance = acctManager.read(acctNum, transaction);// store write request result
                        
                        transaction.write("Transaction #" + transID + " [TransactionManagerWorker.run] READ_REQUEST  <<<<<<<<<<<<<<<<<<<< account #" + acctNum + ", balance $" + balance);
                        send(writer.readResponse(acctNum, balance));
                        break;
                    case "WRITE_REQUEST":
                        // convert message specfic values to correct types
                        transID = Integer.parseInt(args.get(1));
                        acctNum = Integer.parseInt(args.get(2));
                        amount = Integer.parseInt(args.get(3));
                        transaction.write("Transaction #" + transID + " [TransactionManagerWorker.run] WRITE_REQUEST >>>>>>>>>>>>>>>>>>>> account #" + acctNum + ", new balance $" + amount);
                        
                        balance = acctManager.write(acctNum, transaction, amount); // store write request result
                        
                        // check and handle transaction deadlock
                        if (transaction.getStatus() == Transaction.DEADLOCKED) {
                            // swap value to deadlock list
                            deadlockedList.add(transaction);
                            transactionList.remove(transaction);
                            keepOpen = false;
                        }
                        else {
                            // assume no deadlock and send response
                            transaction.write("Transaction #" + transID + " [TransactionManagerWorker.run] WRITE_REQUEST <<<<<<<<<<<<<<<<<<<< account #" + acctNum + ", new balance $" + balance);
                            send(writer.writeResponse(acctNum, balance));
                        }
                        break;
                        
                        
                    case "CLOSE_TRANSACTION":
                        // clear locks
                        TransactionServer.lockManager.unLock(transaction);
                        transaction.write("Transaction #" + transID + " [TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transID);
                        keepOpen = false;
                        
                        // create and send close message
                        send(writer.committed());
                        transaction.setStatus(transaction.FINISHED);
                        transactionList.remove(transaction);
                }
                   
            }
            
            // print operation information
            if (transactionList.isEmpty()){
                
                printDeadlocks();
                
                transaction.write("======================================= BRANCH TOTAL =======================================");
                transaction.write("--->  " + acctManager.getBranchTotal());
                
                log.close();
            }
            try {
                connection.close();
            } catch (IOException e) {
                    System.out.println("Error closing connection");
             }
        }
        
        /**
        * prints out all deadlocks that were caught 
        */
        public void printDeadlocks() {
            log.write("\n======================================= DEADLOCKED ACCOUNTS INFORMATION =======================================");
            
            boolean firstInstance;
            for (Account acct: acctManager.getAccounts()) {
                firstInstance = true;
                for (Transaction dead: deadlockedList) {
                    if (dead.getLockedOn() == acct.getID()){
                        if (firstInstance){
                            log.write("\nAccount #" + acct.getID() + " is involved in deadlock:");
                            firstInstance = false;
                        }
                        log.write("\ttransaction " + dead.getTidStr() + " trying to set WRITE_LOCK, waiting for release of READ_LOCK, held by transaction(s)" + dead.getDeadLock().getHoldingStr());  
                    }
                }
            }
            
            log.write("\n======================================= DEADLOCKED TRANSACTIONS INFORMATION =======================================\n");
            
            for (Transaction dead: deadlockedList) {
                ArrayList<String> history = dead.getRecord();
                
                for (String line: history) {
                    System.out.println(line);
                }
                
                log.write(history);
                System.out.println("\n");
            }
        }
    }
    
    
    
    /**
     * default constructor
     * 
     * @param logFile 
     */
    public TransactionManager(String logFile) {
        this.transactionList = new ArrayList<Transaction>();
        this.deadlockedList = new ArrayList<Transaction>();
    }
}