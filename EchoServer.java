import java.net.*;
import java.io.*;

class EchoThread implements Runnable {
    int id;

    Socket socket;

    EchoThread(int id, Socket socket){
        this.id = id;
        this.socket = socket;
    }

    @Override
    public void run() {

            System.out.printf("Client Connected --> Assigned Thread: %d\n", id);
            try (
                InputStream fromClient = this.socket.getInputStream();
                OutputStream toClient = this.socket.getOutputStream();
            ) {

                while (true){
                    int input = fromClient.read();
                    //System.out.printf("[Thread %d] Message: ")
                    if ( (char)(input) == 'e' ) {
                        break;
                    }
                    else {
                        toClient.write(input);
                    }

                }
                System.out.printf("Client Disconnected --> Releasing Thread: %d\n", this.id);

            } catch (IOException e) {
                System.out.println("Exception caught:");
                System.out.println(e.getMessage());
            }

    }


}

class Server {


    public static void main(String[] args) throws IOException {

        int port = 8080;
        int numberOfThreads = 0;


        try (
            ServerSocket serverSocket = new ServerSocket( port ); //Integer.parseInt(args[0]));

            ) {
            while ( true ){
                if (Thread.activeCount() < 5) {
                    Socket socket = serverSocket.accept();
                    EchoThread clientThread = new EchoThread(numberOfThreads % 4, socket);
                    Thread thread = new Thread(clientThread);
                    thread.start();

                    numberOfThreads++;
                }
            }

            //serverSocket.close();

        } catch (IOException e) {
            System.out.println("Exception caught:");
            System.out.println(e.getMessage());
        }

    }

}
