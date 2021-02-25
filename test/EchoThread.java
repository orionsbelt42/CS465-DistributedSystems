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

       try (
           PrintWriter out =
               new PrintWriter(socket.getOutputStream(), true);
           BufferedReader in = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
       ) {
           String inputLine;
           while ((inputLine = in.readLine()) != null) {
               out.println(inputLine);
           }
       } catch (IOException e) {
           System.out.println("Exception caught when trying to listen on port or listening for a connection");
           System.out.println(e.getMessage());
       }

   }
}
