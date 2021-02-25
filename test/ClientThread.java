import java.io.*;
import java.net.*;

public class ClientThread implements Runnable {

    //Socket socket;


    String hostName;
    int portNumber;
    String message;


    ClientThread(String msg) {
        hostName = "127.0.0.1";
        portNumber = 8080;
        message = msg;
    }

    @Override
    public void run()
    {

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader( new InputStreamReader(echoSocket.getInputStream()) );
        ) {
            //System.out.println("Setup connection");
            out.println(message);
            //System.out.println("sent: " + message);
            //echoSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            // System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName + " for " + message);
            // System.exit(1);
        }
    }

}
