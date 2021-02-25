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

   @Override
   public void run()
   {
       int charInt; // Integer form of byte from client (UTF8)
       int state = 0; // current NFA state

       char charFromClient; // char form of byte from client (UTF8)

       try (
           PrintWriter out =
               new PrintWriter(socket.getOutputStream(), true);
           BufferedReader in = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
       ) {
           ArrayList<String> buffer = new ArrayList<String>();
           String inputLine;
           while ((inputLine = in.readLine()) != null) {
           // while ((inputLine = in.readLine()).equals("END: true")) {
               // out.println(inputLine);
               // System.out.println("recieved: " + inputLine);
               //System.out.println("RECV: " + inputLine);
               buffer.add(inputLine);
           }

           synchronized(this){
               parseMessage(buffer);
           }

       } catch (IOException e) {
           System.out.println("Exception caught when trying to listen on port or listening for a connection");
           System.out.println(e.getMessage());
       }

   }


   public void parseMessage(ArrayList<String> msg){
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

           Node newNode = new Node(id, name, senderIP, senderPort);

           boolean foundCopy = false;

           System.out.printf("%s: %s\n", name, body );
           for (Node other:chat.connections){
               if (newNode.equals(other)){
                   foundCopy = true;
                   System.out.println("FOUND");
               }
           }

           if ( !(foundCopy) )
           {
               ChatNode.connections.add(newNode);
           }


   }
}
