import java.net.*;
import java.io.*;

public class EchoServer
{
    public static void main(String[] args) throws IOException
    {
        int portNumber = 1234; // default port number

        if ( args.length == 1 )
        {
            portNumber = Integer.parseInt(args[0]); // get port number as arg
        }
        else if ( args.length > 1 ) // if they mess up and give too many args
        {
            // prompt user for correction
            System.out.println("Incorrect Arguments Entered: <port number>[default=8080]");
            System.exit(1); // exit
        }

        int count = 0; // number of clients since server start
        int current; // number of current connections

        try (ServerSocket serverSocket = new ServerSocket(portNumber);)
        {
            System.out.println("Listening...");

            while(true)
            {
                Socket socket = serverSocket.accept(); // wait for a connection
                count++; // increment the total number of clients

                // count = Thread.activeCount();
                Runnable run = new EchoThread(socket, count); // create new Runnable
                Thread thread = new Thread(run); // assign Runnable to thread
                thread.start(); // start the thread

                current = Thread.activeCount() - 1;
                // show number of users currently connected
                System.out.println("Clients connected: " + current);
            }
        }
    }
}
