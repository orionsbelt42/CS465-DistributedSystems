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
            /*
            System.out.println(inputSpilt[0]);
            System.out.println(inputSpilt[1]);
            if (inputLine.equals("END: TRUE"))
            {
                System.out.println("");
                break;
            }
            else if (inputLine.equals("ACTION: JOIN"))
            {
                for (Node item: chat.connections)
                {
                    out.println(item.toString());
                }
            }
            */
            System.out.println("\n=============\n" + incoming.toString());
            //System.out.println("\n-----------------------\n" + buffer2);
           /*
           while ((inputLine = in.readLine()) != null) {

               //System.out.println("Found OUT");
               buffer.add(inputLine);
               System.out.println(inputLine);

               if ( inputLine.equals("ACTION: JOIN") )
               {
                   for (Node other:chat.connections)
                   {
                       out.println(other.toString());
                   }
                   out.println("END: TRUE");
                   break;
               }

           }


           System.out.println("\n\nEXITED\n\n\n");


           if (buffer.size() > 0)
           {
               recieved = parseMessage( buffer );

               recieved.showValues();

               if ( recieved.action.equals("JOINED") )
               {
                   chat.connections.add(recieved);
               }
               else if ( recieved.action.equals("LEAVE") )
               {
                   chat.connections.remove(recieved);
               }
               else
               {
                   System.out.println("[" + recieved.name + "] " + recieved.body );
               }
           }
           */
           parseMessage( buffer );

       } catch (IOException e) {
           System.out.println("Exception caught when trying to listen on port or listening for a connection");
           System.out.println(e.getMessage());
       }

   }


   public Node parseMessage(ArrayList<String> msg){
       String line;

       String action = ""; // = msg.get(0).split(": ", 1)[1];
       String name = "";
       int id = 0;
       String senderIP = "127.0.0.1";
       int senderPort = 9090;
       String body = "";

       int index = 0;
       int maxBufferLen = 1000;
       String current = msg.get(index);
       boolean endNotFound = true;
       boolean bodyFound = false;

       while( endNotFound && index < msg.size()){


           String[] args = current.split(": ", -2);

           switch (args[0]){
                case "ACTION":
                    action = args[1].trim();
                    index++;
                    break;
                case "NAME":
                    name = args[1].trim();
                    index++;
                    break;
                case "ID":
                    id = Integer.parseInt(args[1].trim());
                    index++;
                    break;
                case "SENDER":
                    senderIP = args[1].trim().split(" ", -2)[0];
                    senderPort = Integer.parseInt(args[1].trim().split(" ", -2)[1]);
                    index++;
                    break;
                case "BODY":
                    body = args[1].trim()+"\n";
                    bodyFound = true;
                    index++;
                    break;
                case "END":
                    if (args[1].trim().equals("true")){
                        endNotFound = false;
                    }
                    bodyFound = false;
                    break;
                default:
                    if( bodyFound ){
                        body += current;
                    }

                    index++;
                    break;
           }
           current = msg.get(index);
       }
       System.out.println("============================");
       System.out.println("\nBody: " + body);
       System.out.println("Action: "+action);
       Node newNode = new Node(id, name, senderIP, senderPort, action, body);
       return newNode;
   }
}
