import java.net.*;
import java.io.*;
import java.util.*;

public class ChatNode
{
    // store connections
    public static ArrayList<Node> connections = new ArrayList<Node>();
    public static Node myData = new Node();

    String name;


    public static void main(String[] args) throws IOException, InterruptedException
    {
        // set default values
        myData.port = 8080;
        String hostName = "127.0.0.1";
        int port;

        // data type place holders
        Node initial;
        MSG join;
        MSG joined;

        // get user input
        Scanner scan = new Scanner(System.in);
        System.out.println("Hello! Welcome to our chat, please enter your name: ");
        myData.name = scan.next();

        System.out.println("\n");

        // MSG( Node connection, String msgAction, String msgBody )
        if (args.length == 2)
        {
            // if args are passed use them to setup
            System.out.println("===============================================");
            System.out.println("  Setup");
            System.out.println("===============================================");
            hostName = args[0];
            port = Integer.parseInt(args[1]);

            // create new messages
            initial = new Node(0, "", hostName, myData.port);
            join = new MSG( "JOIN" );

            // send init join message
            sendMSG( join, initial );

            joined = new MSG( myData, "JOINED", "" );

            // send follow up
            System.out.println("Sending joined MESSAGE");
            for (Node client: connections)
            {
                sendMSG( joined, client );
            }


        }
        else
        {
            // let user know there are not other nodes
            System.out.println("\nYou're the first one here!\nWaiting for other nodes to connect");

        }



        int count = 0; // number of clients since server start
        int current; // number of current connections

        // init and start server side of node
        Server thread = new Server(myData.port); // assign Runnable to thread
        thread.start(); // start the thread


        count++; // increment the total number of clients


        // main loop prompt user for data and send to other nodes
        String userInput = "";
        MSG newMsg;
        System.out.println("Enter Message or type exit to quit: ");
        do {

            userInput = scan.next();
            System.out.println("Me: "+userInput);

            // check that connection exists before sending
            if (connections.size() > 0){
                newMsg = new MSG(myData, "MESSAGE", userInput);
                for (Node client: connections)
                {
                    sendMSG( newMsg, client );
                }
            }


        } while (!userInput.equals("exit") && !userInput.equals("exit\n"));
        // follow exit path
        if (connections.size() > 0){
            newMsg = new MSG(myData, "LEAVE", "");
            for (Node client: connections)
            {
                sendMSG( newMsg, client );
            }
        }
        // let user know and exit
        System.out.println("Exiting Chat");
        System.exit(1);

    }


    public static void sendMSG(MSG packet, Node receiver )
    {
        // creates new client and sends message for every node
        try (
            Socket socket = new Socket(receiver.hostName, receiver.port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()) );
        ) {
            //System.out.println("Setup connection");

            // System.out.println("sent: " + message);

            if (packet.action.equals("JOIN"))
            {
                System.out.println("Sending JOIN request");
                String[] myConnection = socket.getLocalSocketAddress().toString().substring(1).trim().split(":", -2);
                String host = myConnection[0];
                //int port = Integer.parseInt(myConnection[1]);

                myData.hostName = host;
                //myData.port = port;

                out.println(packet.toString());

                Node newNode;
                String nextNode;
                String[] firstSplit;
                String[] secondSplit;

                String nodeName;
                String nodeHost;
                int nodePort;
                int count = 1;
                while ((nextNode = in.readLine()) != null)
                {
        			if (nextNode.equals("END: TRUE"))
        			{
        				socket.close();
        				break;
        			}
                    else
                    {
                        // ex: 127.0.0.9:8080,Client 9

                        // get ["hostname:port", "name"]
                        firstSplit = nextNode.split(",", -2);
                        nodeName = firstSplit[1];

                        // get ["hostname", "port"]
                        secondSplit = firstSplit[0].split(":", -2);
                        nodeHost = secondSplit[0];

                        // convert port string to int
                        nodePort = Integer.parseInt(secondSplit[1]);

                        newNode = new Node( count, nodeName, nodeHost, nodePort);
                        connections.add(newNode);
                    }
        		}

                myData.port = 8090 + 10*connections.size();
                System.out.println("Reassigned to port: "+ myData.port);

            }

            out.println(packet.toString());

            socket.close();

            // after sending close the socket
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + receiver.hostName);
            // System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                receiver.hostName+":"+receiver.port);
            // System.exit(1);
        }
    }

}
