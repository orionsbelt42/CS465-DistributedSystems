import java.net.*;
import java.io.*;
import utils.*;

public class TransactionServer
{
    
    public static void main(String[] args) throws IOException
    {
        String configFile = "TransactionServer.properties"; // default port number

        if ( args.length == 1 )
        {
            configFile = args[0]; // get server properties file name as arg
        }

        PropertyHandler configData = new PropertyHandler(configFile);
        
        String hostname = configData.getProperty("HOST");
        int port = Integer.parseInt(configData.getProperty("PORT"));
        
        int numberOfAccounts = Integer.parseInt(configData.getProperty("NUMBER_ACCOUNTS"));
        int initialBalance = Integer.parseInt(configData.getProperty("INITIAL_BALANCE"));
        
        
        // create TransactionManager here
            
        // create LockManager here
        
        // create AccountManager here
            // pass number of accts + initial balance
            
        // create ServerSocket here
        
        try (ServerSocket serverSocket = new ServerSocket(port);)
        {
            System.out.println("[TransactionServer.TransactionServer] ServerSocket created");

            while(true)
            {
                Socket socket = serverSocket.accept(); // wait for a connection

                // pass connection to TransactionManager to spawn TransactionManagerThread
                
                
            }
        }
    }
}
