import java.net.*;
import java.io.*;
import java.util.Scanner;

class EchoThread implements Runnable
{
   private Socket socket; // system socket
   private int count; // client number

   InputStream fromClient; // stream for incoming data from client
   OutputStream toClient; // stream for outgoing data from server


   EchoThread(Socket newSocket, int newCount)
   {
       socket = newSocket; // assign server socket to local
       count = newCount; // copy thread number
   }

   @Override
   public void run()
   {
       int charInt; // Integer form of byte from client (UTF8)
       int state = 0; // current NFA state

       char charFromClient; // char form of byte from client (UTF8)

       try
       {
           fromClient = socket.getInputStream(); // bind to stream from client
           toClient = socket.getOutputStream(); // bind to stream to client

           // loop until all bytes are read
           while (true)
           {

                // check for end of stream and quit final state
                if (fromClient.available() == 0 && state == 4)
                {
                    break; // end loop
                }

                charInt = fromClient.read(); // read one byte from client

                charFromClient = (char)charInt; // convert int version of input to char

                // check if char from client is in UTF8 char value range (a-z) or (A-Z)
                if ((charFromClient >= 'a' && charFromClient <= 'z')
                    || (charFromClient >= 'A' && charFromClient <= 'Z') )
                {

                    toClient.write(charInt); // echo char back to client

                    // look for "quit" command from client
                    // implements simple 5 state NFA
                    if (charFromClient == 'q')
                    {
                        state = 1;
                    }

                    else if (charFromClient == 'u' && state == 1)
                    {
                        state = 2;
                    }

                    else if (charFromClient == 'i' && state == 2)
                    {
                        state = 3;
                    }

                    else if (charFromClient == 't' && state == 3)
                    {
                        state = 4;
                    }

                    else
                    {
                        state = 0;
                    }
               }
           }
       }
       // If there are problems, display the error and exit
       catch (IOException ioExec) {System.err.println(ioExec);}
       finally
       {
           System.out.println("Client " + count + " left the session."); // display client exit
       }
   }
}
