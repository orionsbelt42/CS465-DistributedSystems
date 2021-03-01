import java.net.*;
import java.io.*;

public class Server extends Thread
{
    int portNumber;
    public static ChatNode chat;

    Server(int port)
    {
        portNumber = port;
        //chat = calling;
    }
    @Override
    public void run()
    {
        //int portNumber = 8080; // default port number



        try (ServerSocket serverSocket = new ServerSocket(portNumber);)
        {
            System.out.println("Listening... server" +  serverSocket.getLocalSocketAddress());
            while(true)
            {
                Socket socket = serverSocket.accept(); // wait for a connection
                System.out.println("Socket: " + socket.getLocalSocketAddress());
                //System.out.println("[Connected]");

                // count = Thread.activeCount();
                Runnable run = new ServerThread(socket); // create new Runnable
                Thread thread = new Thread(run); // assign Runnable to thread
                thread.start(); // start the thread



            }
        } catch (IOException e) {
            System.err.println("Server Failed");
            // System.exit(1);
        }
    }
}
