import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TransactionClient
{
    public static void main(String[] args) throws IOException
    {
        Socket socket = null; // socket here

        // get and parse connection arguments
        int portNumber = 1234; // default port number
        String hostname = "127.0.0.1"; // localhost

        if ( args.length == 1 )
        {
            hostname = args[0]; // get server hostname
        }
        else if( args.length == 2 )
        {
            hostname = args[0]; // get server hostname
            portNumber = Integer.parseInt(args[1]); // get server port number
        }
        else if ( args.length > 2 ) // prompt user if incorrect
        {
            System.out.println("Incorrect Arguments: <hostname> <port number>[default=8080]");
            System.exit(1);
        }

        // setup connection to server
        try
        {
            Socket newSocket = new Socket(hostname, portNumber); // connect to socket

            InputStream fromServer = newSocket.getInputStream(); // stream from server
            OutputStream toServer = newSocket.getOutputStream(); // stream to server

            Scanner scan = new Scanner(System.in); // scanner obj for user input

            while (true)
            {
                system.out.println("Type 20000 to quit at any time");
                system.outprintln("How many accounts do you want to create(integer values only)? ");
                int accounts = scan.nextInt(); // get user input for accounts
                system.outprintln("How much money do you want to put in each account(integer values only)? ");
                int money = scan.nextInt(); // get user input for accounts
                system.outprintln("How many transactions do you want to perform(integer values only)? ");
                int transactions = scan.nextInt(); // get user input for accounts
                system.out.println("You want to make " + accounts + " accounts, each with $" + money + " and perform " + transactions + " transactions.");
                String output = ""; // store output here

                // get and store expected filtered length
                int outputLen = sendBytes(money, accounts, transactions, toServer);

                output = recvBytes( fromServer); // get and store output from server

                if ( accounts == 20000 || transactions == 20000){ // if user types 2000 close connection
                    break; // exit loop
                }

            }
        }
        catch (UnknownHostException e) // deal with any errors
        {
            System.err.println("Connection Failed: Unknown Host [" + hostname + "]");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't connect to host [" + hostname + ":" + portNumber + "]");
            System.exit(1);
        }

    }

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

}
