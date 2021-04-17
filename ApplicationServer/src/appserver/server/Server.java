package appserver.server;

import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.comm.ConnectivityInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Integer.valueOf;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;
import utils.PropertyHandler;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Server {

    // Singleton objects - there is only one of them. For simplicity, this is not enforced though ...
    static SatelliteManager satelliteManager = null;
    static LoadManager loadManager = null;
    static ServerSocket serverSocket = null;

    private int port;
    private String host;
    private ServerSocket serverSock;
    public Server(String serverPropertiesFile) {
        
        // create satellite manager and load manager
        // ...
        
        loadManager = new LoadManager();
        
        // read server properties and create server socket
        // ...
        
        try {
            File myfp = new File( serverPropertiesFile );
            Scanner myScanner = new Scanner(myfp);
            while (myScanner.hasNextLine()) {
                String data = myScanner.nextLine();
                String values[] = data.split("\\t");
                if (values.length > 1)
                {
                    if (values[0].trim().equals("PORT"))
                    {
                        this.port = Integer.parseInt(values[1]);
                        System.out.println("ApplicationSever port " + this.port);
                    }
                    else if (values[0].trim().equals("HOST"))
                    {
                        this.host = values[1];
                        System.out.println("ApplicationSever host " + this.host);
                    }
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            this.serverSock = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println("Failed to create ServerSocket: " + e);
        }
    }

    public void run() {
        // serve clients in server loop ...
        // when a request comes in, a ServerThread object is spawned
        // ...

        while (true) 
        {
            try {
                Socket clientSocket = serverSock.accept();
                
                ServerThread client = new ServerThread(clientSocket);
                client.run();
            } catch (IOException e) {
                System.err.println("Failed to create client Socket: " + e);
            }
        }
    
    }

    // objects of this helper class communicate with satellites or clients
    private class ServerThread extends Thread {

        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;
        
       // String satelliteName;

        private ServerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            // set up object streams and read message
            // ...
            try {
                writeToNet = new ObjectOutputStream(client.getOutputStream());
                readFromNet = new ObjectInputStream(client.getInputStream());
                
                // reading message
                message = (Message) readFromNet.readObject(); 
            } catch (IOException e) {
                System.err.println("Failed to create client socket IO streams: " + e);
            } 
            
            catch (ClassNotFoundException e) {
                System.err.println("Failed to create client socket IO streams: " + e);
            } 

            
            // process message
            switch (message.getType()) {
                case REGISTER_SATELLITE:
                    // read satellite info
                    // ...
                    
                    // register satellite
                    synchronized (Server.satelliteManager) {
                        // ...
                        Server.satelliteManager.registerSatellite((ConnectivityInfo) message.getContent());
                        
                    }

                    // add satellite to loadManager
                    synchronized (Server.loadManager) {
                        // ...
                    }

                    break;

                case JOB_REQUEST:
                    System.err.println("\n[ServerThread.run] Received job request");

                    String satelliteName = null;
                    ConnectivityInfo satelliteInfo = null;
                    synchronized (Server.loadManager) {
                        // get next satellite from load manager
                        // ...
                        try {
                            satelliteName = Server.loadManager.nextSatellite();
                        } catch (Exception e) {
                            System.err.println("Failed to get next satellite: " + e);
                        }
                        
                        // get connectivity info for next satellite from satellite manager
                        // ...
                        
                        satelliteInfo = Server.satelliteManager.getSatelliteForName(satelliteName);
                    }

                    Socket satellite = null;
                    ObjectInputStream readFromSat = null;
                    ObjectOutputStream writeToSat = null;
                    
                    
                    // connect to satellite
                    // ...
                    try {
                        satellite = new Socket(satelliteInfo.getHost(), satelliteInfo.getPort());
                        readFromSat = new ObjectInputStream(satellite.getInputStream());
                        writeToSat =  new ObjectOutputStream(satellite.getOutputStream());
                        
                        writeToSat.writeObject(message);
                        message = (Message) readFromSat.readObject();
                        
                        writeToNet.writeObject(message);
                        
                        
                        
                    } catch (IOException e) {
                        System.err.println("Failed to create socket: " + e);
                    } 
                    catch (ClassNotFoundException e) {
                        System.err.println("Failed to create client socket IO streams: " + e);
                    }
                    // open object streams,
                    // forward message (as is) to satellite,
                    // receive result from satellite and
                    // write result back to client
                    // ...

                    break;

                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }

    // main()
    public static void main(String[] args) {
        // start the application server
        Server server = null;
        if(args.length == 1) {
            server = new Server(args[0]);
        } else {
            server = new Server("../../config/Server.properties");
        }
        server.run();
    }
}
