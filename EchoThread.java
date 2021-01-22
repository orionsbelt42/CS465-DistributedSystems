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
           // String buffer = "";
           char charFromClient;
           boolean writeBuffer = false;
           int state = 0;
           while (true){
               //System.out.print("[" + fromClient.available() + "]"  + '\n');
               if (fromClient.available() == 0 && state == 4){
                   break;
               }

               charInt = fromClient.read();

               charFromClient = (char)charInt;
               //if ((charInt >= Character.getNumericValue('a') && charInt <= Character.getNumericValue('z')) || (charInt >= Character.getNumericValue('A') && charInt <= Character.getNumericValue('Z')) ){

               if ((charInt >= (int)'a' && charInt <= (int)'z') || (charInt >= (int)'A' && charInt <= (int)'Z') ) {
                    toClient.write(charInt);

                    // System.out.print("[" + fromClient.available() + "]" + charFromClient + '\n');
                    if (charFromClient == 'q') {
                        state = 1;
                    }

                    else if (charFromClient == 'u' && state == 1){
                        state = 2;
                    }

                    else if (charFromClient == 'i' && state == 2){
                        state = 3;
                    }

                    else if (charFromClient == 't' && state == 3){
                        state = 4;
                    }
                    /**
                    else if (charFromClient == (char)charInt && state == 4){
                        state = 0;
                    }
                    **/
                    else {
                        state = 0;
                    }

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
