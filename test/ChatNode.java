import java.net.*;
import java.io.*;

public class ChatNode
{
    public static void main(String[] args) throws IOException, InterruptedException
    {


        int count = 0; // number of clients since server start
        int current; // number of current connections

        int num = 0;

        Server thread = new Server(); // assign Runnable to thread
        thread.start(); // start the thread
        //Thread.sleep(4000);

        //Socket socket = serverSocket.accept(); // wait for a connection
        count++; // increment the total number of clients

        // count = Thread.activeCount();
        //Runnable run = new ServerThread(serverSocket); // create new Runnable

        System.out.println("Creating clients");
        int temp = 0;
        while ( temp < 10 ){
            Runnable client = new ClientThread("Client: "+ temp);
            Thread cThread = new Thread(client);
            cThread.start();

            temp++;
        }


    }
}
