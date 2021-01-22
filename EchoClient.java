import java.io.*;
import java.net.*;
import java.util.Scanner;

public class EchoClient
{
    public static void main(String[] args) throws IOException
    {
        Socket socket = null;
        String str = null;
        //BufferedReader buffread = null;
        //DataOutputStream dos = null;
        //BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        // get and parse connection arguments
        int portNumber = 1234; // default port number
        String hostname = "127.0.0.1"; // localhost

        if ( args.length == 1 )
        {
            hostname = args[0];
        }
        else if( args.length == 2 )
        {
            hostname = args[0];
            portNumber = Integer.parseInt(args[1]);
        }
        else if ( args.length > 2 )
        {
            System.out.println("Incorrect Arguments Entered: <hostname> <port number>[default=8080]");
            System.exit(1);
        }

        // setup connection to server
        try {
            Socket echoSocket = new Socket(hostname, portNumber); // connect to socket
            //PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);

            InputStream fromServer = echoSocket.getInputStream();
            OutputStream toServer = echoSocket.getOutputStream();

            Scanner scan = new Scanner(System.in);
            while (true)
            {
                String userInput = scan.nextLine() + '\n';
                String output = "";
                int outputLen = sendBytes(userInput, toServer);

                output = recvBytes(outputLen, fromServer);

                /**
                toServer.write(userInput.getBytes("UTF8"));
                System.out.print(fromServer.read());
                **/
            }
        } catch (UnknownHostException e) {
            System.err.println("Connection Failed: Unknown Host [" + hostname + "]");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't connect to server @ host [" + hostname + ":" + portNumber + "]");
            System.exit(1);
        }

    }

    private static int sendBytes( String out, OutputStream stream ) {
        try {
            int filteredLen = 0;
            byte[] bytesOut = out.getBytes("UTF8");
            stream.write(bytesOut);
            System.out.print("Sent: " + out);

            for (int i = 0; i < bytesOut.length; i++){
                if ((bytesOut[i] >= (int)'a' && bytesOut[i] <= (int)'z') || (bytesOut[i] >= (int)'A' && bytesOut[i] <= (int)'Z') ) {
                    filteredLen++;
                }
            }
            return filteredLen;
        } catch (UnsupportedEncodingException e){
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        return 0;
    }


    private static String recvBytes( int length, InputStream stream ) {
        String buffer = "";
        try {
            System.out.print("Recieved: ");
            for (int recvIndex = 0; recvIndex < length; recvIndex++ ){
                buffer += (char)stream.read();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println(buffer);

        return buffer;
    }

}
