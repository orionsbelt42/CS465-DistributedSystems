import java.io.*;
import java.net.*;
import java.util.Scanner;

public class EchoClient
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
            Socket echoSocket = new Socket(hostname, portNumber); // connect to socket

            InputStream fromServer = echoSocket.getInputStream(); // stream from server
            OutputStream toServer = echoSocket.getOutputStream(); // stream to server

            Scanner scan = new Scanner(System.in); // scanner obj for user input

            while (true)
            {
                String userInput = scan.nextLine(); // get user input
                String output = ""; // store output here

                // get and store expected filtered length
                int outputLen = sendBytes(userInput, toServer);

                output = recvBytes(outputLen, fromServer); // get and store output from server

                if (output.equals("quit")){ // if user types quit close connection
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

    private static int sendBytes( String out, OutputStream stream )
    {
        try
        {
            int filteredLen = 0; // store filtered length here
            byte[] bytesOut = out.getBytes("UTF8"); // force encoding
            stream.write(bytesOut); // send data
            System.out.println("Sent: " + out); // show sent data

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


    private static String recvBytes( int length, InputStream stream )
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
