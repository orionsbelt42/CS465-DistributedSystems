import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";
        int portNumber = 8080;

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            String[] userInput = {"ACTION: Join\n", "NAME: client 1\n", "ID: 0\n", "SENDER: 127.0.0.1 8090\n", "BODY: this is a test message\n", "And this is the nextline\n", "END: true\n"};
            String test = "ACTION: Join\nNAME: anton\nID: 0\nSENDER: 127.0.0.1 8090\nBODY: this is a test message\nEND: true\n";
            for (String i: userInput){
                out.println(i);
            }
                //out.println(test);


                //System.out.println("echo: " + in.readLine());

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}
