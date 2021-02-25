import java.net.*;
import java.io.*;
import java.util.*;

public class ChatNode
{

    public static ArrayList<Node> connections = new ArrayList<Node>();
    public static void main(String[] args) throws IOException, InterruptedException
    {


        int count = 0; // number of clients since server start
        int current; // number of current connections

        int num = 8080;

        Server thread = new Server(num); // assign Runnable to thread
        thread.start(); // start the thread
        //Thread.sleep(4000);

        //Socket socket = serverSocket.accept(); // wait for a connection
        count++; // increment the total number of clients

        // count = Thread.activeCount();
        //Runnable run = new ServerThread(serverSocket); // create new Runnable


        /*
        System.out.println("Creating clients");
        int temp = 0;
        while ( temp < 10 ){
            Runnable client = new ClientThread("Client: "+ temp);
            Thread cThread = new Thread(client);
            cThread.start();

            temp++;
        }
        */
        Thread.sleep(10000);
        for (Node i:connections)
        {System.out.println(i.toString());}

    }

}
