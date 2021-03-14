import java.io.*;
import java.util.Scanner;

class TransactionThread implements Runnable
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
                if (fromClient.available() == 20000)
                {
                    break; // end loop
                }

                int money = fromClient[0];
                int accounts = fromClient[1];
                int transactions = fromClient[2];

                }
            }

            // If there are problems, display the error and exit
            catch (IOException ioExec)
            { System.err.println(ioExec); } // display the io exception
            
            finally
            {
                System.out.println("Client " + count + " left the session."); // display client exit
            }
        }
    }
