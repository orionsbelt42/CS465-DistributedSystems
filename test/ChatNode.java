import java.net.*;
import java.io.*;
import java.util.*;

public class ChatNode
{

    public static ArrayList<Node> connections = new ArrayList<Node>();

    String name;


    public static void main(String[] args) throws IOException, InterruptedException
    {
        int count = 0; // number of clients since server start
        int current; // number of current connections

        int num = 8080;

        for (int i=0; i< 10;i++){
            Node temp = new Node(0, "Client " + i, "127.0.0."+i, 8080);
            connections.add(temp);

        }

        Server thread = new Server(num); // assign Runnable to thread
        thread.start(); // start the thread
        //Thread.sleep(4000);

        //Socket socket = serverSocket.accept(); // wait for a connection
        count++; // increment the total number of clients

        // count = Thread.activeCount();
        //Runnable run = new ServerThread(serverSocket); // create new Runnable
        System.out.println("Chat Start");



        Scanner scan = new Scanner(System.in);
        /*
        System.out.println("Welcome, Please enter a username to get started");
        name = scan.next();


        String userInput;


        do {
            userInput = scan.next();
            //sendChatMessage( userInput );

        } while (!userInput.equals("exit"));

        */
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


    }

    /*
    public void sendChatMessage( String message )
    {
        String packet = "ACTION: Message\nNAME: " + name + "\nSENDER:";
        for (Node client: this.connections)
        {
            try (
                Socket echoSocket = new Socket(client.hostName, client.port);
                PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                    new BufferedReader(
                        new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                    new BufferedReader(
                        new InputStreamReader(System.in))
            ) {


                out.println(message);


            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + hostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
                System.exit(1);
            }
        }

    }
    */
}
