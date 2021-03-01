import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;

class ServerThread implements Runnable
{
    //private final Lock queueLock = new ReentrantLock();
   private Socket socket; // system socket
   private int number;
   InputStream fromClient; // stream for incoming data from client
   OutputStream toClient; // stream for outgoing data from server

   boolean working = false;

   ChatNode chat;


   ServerThread(Socket newSocket, int count)
   {
       // System.out.println("[Listening for new connection]");
       socket = newSocket;
       number = count;
       //chat = calling;
   }

   private String getBytes( InputStream in )
   {
       String buffer = "";
       byte[] byteBuffer;

       try {
           while ( in.available() > 0 )
           {
               byteBuffer = new byte[in.available()];
               in.read(byteBuffer);
               buffer = buffer + new String(byteBuffer);
           }
           return buffer;
       } catch (IOException e) {
           System.out.println("Exception caught when trying to listen on port or listening for a connection");
           System.out.println(e.getMessage());
       }
       return "";
   }

   @Override
   public void run()
   {
       int charInt; // Integer form of byte from client (UTF8)
       int state = 0; // current NFA state
       Node recieved;

       char charFromClient; // char form of byte from client (UTF8)
       //System.out.println("Socket: " + socket.getLocalSocketAddress());
       try (
           PrintWriter out =
               new PrintWriter(socket.getOutputStream(), true);
           BufferedReader in = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
       ) {
            ArrayList<String> buffer = new ArrayList<String>();
            String inputLine;
            String[] inputSpilt;
            String buffer2 = "";
            MSG incoming = new MSG();
            while ((inputLine = in.readLine()) != null) {


                buffer.add(inputLine);

                inputSpilt = inputLine.split(": ", -2);

                // get values from recieved data
                if (inputSpilt.length == 2){
                    switch( inputSpilt[0] )
                    {
                        case "ACTION":
                            incoming.action = inputSpilt[1];
                            break;
                        case "NAME":
                            incoming.name = inputSpilt[1];
                            break;
                        case "HOSTNAME":
                            incoming.hostName = inputSpilt[1];
                            break;
                        case "PORT":
                            incoming.port = Integer.parseInt(inputSpilt[1]);
                            break;
                        case "ID":
                            incoming.id = Integer.parseInt(inputSpilt[1]);
                            break;
                        case "END":
                            if (inputSpilt[1].equals("TRUE"))
                            {
                                incoming.end = true;
                            }
                            else
                            {
                                incoming.end = false;
                            }
                            break;
                        case "BODY":

                            incoming.body = inputSpilt[1] + "\n";
                    }
                }
                // check for end of message
                if (inputLine.equals("END: TRUE"))
                {
                    break;
                }

            }

            // handle message types
            if (incoming.action.equals("JOIN"))
            {
                // return list of connections
                out.println(ChatNode.myData.toString());
                for (Node item: ChatNode.connections)
                {
                    out.println(item.toString());
                }

                out.println("END: TRUE\n");
            }
            else if (incoming.action.equals("JOINED"))
            {
                // Node(int newId, String newName, String newHostName, int newPortNumber)
                if (incoming.name.equals(""))
                {
                    incoming.name = "Client " + number;
                }
                Node newNode = new Node( incoming.id, incoming.name, incoming.hostName, incoming.port );
                ChatNode.connections.add(newNode);

                System.out.println("[" + incoming.name + " joined the chat]");
            }
            else if (incoming.action.equals("MESSAGE"))
            {
                System.out.println(incoming.name + ": " + incoming.body);
            }
            else if (incoming.action.equals("LEAVE"))
            {
                // remove connection no response needed
                Node newNode = new Node( incoming.id, incoming.name, incoming.hostName, incoming.port );
                ChatNode.connections.remove(newNode);
                System.out.println("[" + incoming.name + " left the chat]");
            }
            else if (incoming.action.equals("UPDATE"))
            {
                Node newNode = new Node( incoming.id, incoming.name, incoming.hostName, incoming.port );
                ChatNode.connections.remove(newNode);
                ChatNode.connections.add(newNode);
            }



       } catch (IOException e) {
           System.out.println("Exception caught when trying to listen on port or listening for a connection");
           System.out.println(e.getMessage());
       }

   }

}
