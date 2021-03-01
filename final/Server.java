import java.net.*;
import java.io.*;

public class Server extends Thread
{
    int portNumber;
    public static ChatNode chat;
    private int count = 0;

    Server(int port)
    {
        portNumber = port;
        //chat = calling;
    }
    @Override
    public void run()
    {
        //int portNumber = 8080; // default port number

        boolean myDataIsSet = false;

        System.out.println("Setting up server on port: "+ portNumber +"\n\n");

        System.out.println("===============================================");
        System.out.println("  Chat");
        System.out.println("===============================================");

        // setup server socket
        try (ServerSocket serverSocket = new ServerSocket(portNumber);)
        {
            //System.out.println("Listening... server" +  serverSocket.getLocalSocketAddress());
            while(true)
            {
                count += 1;
                Socket socket = serverSocket.accept(); // wait for a connection
                if (!myDataIsSet)
                {
                    String[] myConnection = socket.getLocalSocketAddress().toString().substring(1).trim().split(":", -2);
                    String host = myConnection[0];

                    ChatNode.myData.hostName = host;
                    ChatNode.myData.port = portNumber;

                    myDataIsSet = true;
                }
                // run a thread for each new connection
                Runnable run = new ServerThread(socket, count); // create new Runnable
                Thread thread = new Thread(run); // assign Runnable to thread
                thread.start(); // start the thread



            }
        } catch (IOException e) {
            System.err.println(e +": Server Failed");
            // System.exit(1);
        }
    }
}
