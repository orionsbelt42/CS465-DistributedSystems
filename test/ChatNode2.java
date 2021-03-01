import java.net.*;
import java.io.*;

public class ChatNode2
{
    public static void main(String[] args) throws IOException, InterruptedException
    {


        int port = 8080; // number of clients since server start
        String host = "127.0.0.1"; // number of current connections

        int num = 0;

        Server thread = new Server(port); // assign Runnable to thread
        thread.start(); // start the thread
        //Thread.sleep(4000);

        //Socket socket = serverSocket.accept(); // wait for a connection

        // count = Thread.activeCount();
        //Runnable run = new ServerThread(serverSocket); // create new Runnable



    }
}
