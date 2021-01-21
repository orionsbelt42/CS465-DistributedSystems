import java.net.*;
import java.io.*;
import java.util.Scanner;

class EchoThread implements Runnable
{
    private Socket socket;
    private int count;
    private long id;
    InputStream fromClient;
    OutputStream toClient;
    private PrintWriter printWrite;

    EchoThread(Socket newSocket, int newCount)
    {
        socket = newSocket;
        count = newCount;
    }

    @Override
    public void run()
    {
        try
        {
            fromClient = socket.getInputStream();
            toClient = socket.getOutputStream();
        }
        catch (IOException ioExec) {System.err.println(ioExec);}
        try (Scanner scan = new Scanner(fromClient))
        {
            printWrite = new PrintWriter(toClient, true);
            while(scan.hasNextLine())
            {
                String line = scan.nextLine();
                System.out.println("Client " + count + ": " + line);
                printWrite.println(line);     
            }
        }
        finally {
            System.out.println("Client " + count + " left the session.");
        }  
    }
}