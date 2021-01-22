import java.net.*;
import java.io.*;

public class EchoServer
{
    public static void main(String[] args) throws IOException
    {
        int portNumber = 1234; // default port number
        int numberOfThreads = 2; // localhost

        if ( args.length == 1 )
        {
            portNumber = Integer.parseInt(args[0]);
        }
        else if ( args.length == 2 )
        {
            portNumber = Integer.parseInt(args[0]);
            numberOfThreads = Integer.parseInt(args[1]);
        }
        else if ( args.length > 2 )
        {
            System.out.println("Incorrect Arguments Entered: <port number>[default=8080] <threads>[default=2]");
            System.exit(1);
        }

        int count = 0;
        long[] ids = new long[10];
        //System.out.println("Clients connected: " + Thread.activeCount());

        try (ServerSocket serverSocket = new ServerSocket(1234))
        {
            System.out.println("Listening...");

            while(true)
            {
                Socket socket = serverSocket.accept();
                count = Thread.activeCount();
                Runnable run = new EchoThread(socket, count);
                Thread thread = new Thread(run);
				        System.out.println("Clients connected: " + count);
                thread.start();

            }
        }
    }         
}