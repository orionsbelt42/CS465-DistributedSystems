package transactionserver;

import java.net.*;
import java.io.*;
import utils.*;

public class TransactionServer
{
    // create LockManager here
    public static LockManager lockManager = new LockManager(); // need to work on this class still
    
    public static SystemLog log = new SystemLog("server.log");
    
    public static void main(String[] args) throws IOException
    {
        String configFile = "TransactionServer.properties"; // default property file name
        String logFile = "server_log.txt";

        // if there is one arg passed when the program is loaded
        if ( args.length == 1 )
        {
            configFile = args[0]; // get server properties file name as arg
        }
        
        // read and store config data from server properties file
        PropertyHandler configData = new PropertyHandler(configFile);
        
        // the hostname/ip of the server
        String hostname = configData.getProperty("HOST");
        // port to bind the server to
        int port = Integer.parseInt(configData.getProperty("PORT"));
        
        // number of accounts to create/use 
        int numberOfAccounts = Integer.parseInt(configData.getProperty("NUMBER_ACCOUNTS"));
        // initial balance for new accounts
        int initialBalance = Integer.parseInt(configData.getProperty("INITIAL_BALANCE"));
        
        boolean applyLocking = Boolean.parseBoolean(configData.getProperty("APPLY_LOCKING"));
        boolean transView = Boolean.parseBoolean(configData.getProperty("TRANSACTION_VIEW"));
        
        // create TransactionManager here
        TransactionManager transManager = new TransactionManager(logFile); // need to work on this class still
        System.out.println("[TransactionServer.TransactionServer] TransactionManager created");
        
        System.out.println("[TransactionServer.TransactionServer] LockManager created");
        // create AccountManager here
            // pass number of accts + initial balance
        AccountManager acctManager = new AccountManager( numberOfAccounts, initialBalance );
        System.out.println("[TransactionServer.TransactionServer] AccountManager created");    
        // create ServerSocket here
        try (ServerSocket serverSocket = new ServerSocket(port);)
        {
            System.out.println("[TransactionServer.TransactionServer] ServerSocket created");
            int count = 0;
            while(true)
            {
                Socket socket = serverSocket.accept(); // wait for a connection
                TransactionManager.TransactionManagerWorker workerThread = transManager.new TransactionManagerWorker(socket, acctManager, count);
                // pass connection to TransactionManager to spawn TransactionManagerThread
                count++;
                Thread thread = new Thread(workerThread); // assign Runnable to thread
                thread.start(); // start the thread
            }
        } catch (IOException e) {
            System.out.println("Error trying to open server socket on " + hostname + ":" + port);
        }
        
        log.write("======================================= BRANCH TOTAL =======================================");
        log.write("--->  " + acctManager.getBranchTotal());
    }
}
