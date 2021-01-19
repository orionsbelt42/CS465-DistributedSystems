import java.io.*;
import java.net.*;

public class EchoClient
{
    public static void main(String[] args)
    {

        int portNumber = 8080; // default port number
        String hostname = "127.0.0.1"; // localhost

        if ( args.length == 1 )
        {
            hostname = args[0];
        }
        else if ( args.length == 2 )
        {
            hostname = args[0];
            portNumber = Integer.parseInt(args[1]);;
        }
        else if ( args.length > 2 )
        {
            System.out.println("Incorrect Arguments Entered: <hostname> <port number>[default=8080]");
            System.exit(1);
        }

    }
}
