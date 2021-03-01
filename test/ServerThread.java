import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;

class ServerThread implements Runnable
{
    //private final Lock queueLock = new ReentrantLock();
   private Socket socket; // system socket

   InputStream fromClient; // stream for incoming data from client
   OutputStream toClient; // stream for outgoing data from server

   boolean working = false;

   ChatNode chat;


   ServerThread(Socket newSocket)
   {
       // System.out.println("[Listening for new connection]");
       socket = newSocket;
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
            // while ((inputLine = in.readLine()).equals("END: true")) {
                // out.println(inputLine);
                System.out.println("recieved: " + inputLine);
                // System.out.println("recieved: " + inputLine);
                //System.out.println("RECV: " + inputLine);
                buffer.add(inputLine);
                //buffer2 += inputLine + "\n";


                inputSpilt = inputLine.split(": ", -2);
                if (inputSpilt.length == 2){
                    switch( inputSpilt[0] )
                    {
                        case "ACTION":
                            incoming.action = inputSpilt[1] + "\n";
                            break;
                        case "NAME":
                            incoming.name = inputSpilt[1]  + "\n";
                            break;
                        case "SENDER":
                            String[] temp = inputSpilt[1].split(" ", -2);
                            incoming.hostName = temp[0]  + "\n";
                            incoming.port = Integer.parseInt(temp[1]);
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
                else
                {
                    incoming.body += inputLine + "\n";
                }

                if (inputLine.equals("END: TRUE"))
                {
                    System.out.println("");
                    break;
                }

            }

            if (incoming.action.equals("JOIN\n"))
            {
                for (Node item: chat.connections)
                {
                    out.println(item.toString());
                }
            }
            else if (incoming.action.equals("JOINED\n"))
            {
                // Node(int newId, String newName, String newHostName, int newPortNumber)
                Node newNode = new Node( incoming.id, incoming.name, incoming.hostName, incoming.port );
                chat.connections.add(newNode);
            }
            else if (incoming.action.equals("MESSAGE\n"))
            {
                System.out.println(incoming.name + ": " + incoming.body);
            }
            else if (incoming.action.equals("LEAVE\n"))
            {
                Node newNode = new Node( incoming.id, incoming.name, incoming.hostName, incoming.port );
                chat.connections.remove(newNode);
            }
            else if (incoming.action.equals("UPDATE\n"))
            {
                Node newNode = new Node( incoming.id, incoming.name, incoming.hostName, incoming.port );
                chat.connections.remove(newNode);
                chat.connections.add(newNode);
            }


            System.out.println("\n=============\n" + incoming.toString());


       } catch (IOException e) {
           System.out.println("Exception caught when trying to listen on port or listening for a connection");
           System.out.println(e.getMessage());
       }

   }

}
