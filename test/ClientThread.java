import java.io.*;
import java.net.*;


public class ClientThread implements Runnable {

    //Socket socket;
    String hostName;
    int portNumber;
    String message;


    // create new client thread
    ClientThread(String host, int port, String msg) {
        this.hostName = host;
        this.portNumber = port;
        this.message = msg;
    }

    @Override   
    public void run()
    {

        try (
            // create new socket
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader( new InputStreamReader(echoSocket.getInputStream()) );
        ) { 
            // output the message
            out.write(message);

            // close the socket
            echoSocket.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
  
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName + " for " + message);

        }
    }

}
