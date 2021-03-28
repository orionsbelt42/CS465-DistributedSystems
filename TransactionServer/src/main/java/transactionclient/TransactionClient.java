package transactionclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import utils.*;


public class TransactionClient
{
    public static void main(String[] args) throws IOException
    {
        String configFile = "TransactionClient.properties"; // default property file name

        // if there is one arg passed when the program is loaded
        if ( args.length == 1 )
        {
            configFile = args[0]; // get client properties file name as arg
        }
        
        // read and store config data from server properties file
        PropertyHandler configData = new PropertyHandler(configFile);
        
        // number of accounts on the server might be able to share on connect
        int numberOfAccounts = Integer.parseInt(configData.getProperty("NUMBER_ACCOUNTS"));
        
        // number of transactions to create/request
        int numberTrans = Integer.parseInt(configData.getProperty("NUMBER_TRANSACTIONS"));
        
        // the hostname/ip of the server
        String hostname = configData.getProperty("SERVER_IP");
        
        // port that server is listening to
        int port = Integer.parseInt(configData.getProperty("SERVER_PORT"));
        
        // initialize API object for the client
        TransactionAPI transaction = new TransactionAPI(configData);
        
        // loop over number of transactions
        
            // create random transaction
                // pick two random accounts 
                
                // pick random int value to transfer
                
            // use API to request transfer 

    }
    
    
    /*
    Unsure if needed, possibly overwritten in API class
    private static int sendTransaction( int money,  int accounts, int transactions, OutputStream stream )
    {
        try
        {
            int filteredLen = 0; // store filtered length here
            List<Integer> info = new ArrayList<Integer>();
            info.add(money);
            info.add(accounts);
            info.add(transactions);
            stream.write(info); // send data
            System.out.println("Request sent to Transactional Server"); // show data is being sent off

            for (int i = 0; i < bytesOut.length; i++) // loop over chars
            {
                // get filtered length for return
                if ((bytesOut[i] >= (int)'a' && bytesOut[i] <= (int)'z')
                        || (bytesOut[i] >= (int)'A' && bytesOut[i] <= (int)'Z') )
                {
                    filteredLen++;
                }
            }
            return filteredLen; // return the length of the expected server response
        }
        catch (UnsupportedEncodingException e) // deal with exceptions
        {
            System.out.println(e);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

        return 0; // on failure return 0
    }


    private static String recvTransaction( int length, InputStream stream )
    {
        String buffer = ""; // store revieved bytes here
        try
        {
            // loop while there is still expected length
            for (int recvIndex = 0; recvIndex < length; recvIndex++ )
            {
                buffer += (char)stream.read(); // read and store from input stream
            }

            System.out.println("Recieved: " + buffer); // print recieved string for checking

            // if quit is returned
            if (length > 0 && buffer.substring( length - 4, length ).equals("quit"))
            {
                return "quit"; // send quit to caller
            }

        }
        catch (IOException e) // deal with errors
        {
            System.out.println(e);
        }

        return buffer;
    }
*/

}
