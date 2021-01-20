import java.net.*;
import java.io.*;
 
public class EchoServer 
{
    public static void main(String[] args) throws IOException
    {
        int count = 0;
        long[] ids = new long[10];

        try (ServerSocket serverSocket = new ServerSocket(1234))
        {
            System.out.println("Listening...");

            while(true)
            {
                count++;
                Socket socket = serverSocket.accept();
                Runnable run = new EchoThread(socket, count);
                Thread thread = new Thread(run);
				System.out.println("Clients connected: " + count);
                thread.start();

               if(Thread.activeCount() != count + 1) 
                {
                    count = Thread.activeCount();
                }

            }
        }
    }         
}