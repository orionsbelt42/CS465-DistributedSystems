import java.net.*;
import java.io.*;
import java.util.Scanner;

class EchoThread implements Runnable
{
   private Socket socket;
   private int count;
   // private long id;
   InputStream fromClient;
   OutputStream toClient;
   // private PrintWriter printWrite;

   EchoThread(Socket newSocket, int newCount)
   {
       socket = newSocket;
       count = newCount;
   }

   @Override
   public void run()
   {
       try
       {
           // System.out.println(count);
           fromClient = socket.getInputStream();
           toClient = socket.getOutputStream();
       }
       catch (IOException ioExec) {System.err.println(ioExec);}
       try {
           int charInt;
           String buffer = "";
           char charFromClient;

           while (true){

               charInt = fromClient.read();
               charFromClient = (char)charInt;
               //if ((charInt >= Character.getNumericValue('a') && charInt <= Character.getNumericValue('z')) || (charInt >= Character.getNumericValue('A') && charInt <= Character.getNumericValue('Z')) ){

               if ((charInt >= (int)'a' && charInt <= (int)'z') || (charInt >= (int)'A' && charInt <= (int)'Z') ) {
                    toClient.write(charInt);
               }
               else
               {
                   buffer += (char)charInt;
               }

           }

           /**
           //printWrite = new PrintWriter(toClient, true);
           while(scan.hasNextLine())
           {
               String line = scan.nextLine();
               System.out.println("Client " + count + ": " + line);
               printWrite.println(line);
           }
           **/
       }
       catch (IOException ioExec) {System.err.println(ioExec);}
       finally {
           System.out.println("Client " + count + " left the session.");
       }
   }
}
