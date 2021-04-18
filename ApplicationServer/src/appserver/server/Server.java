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
        System.out.println("Server Starting: ");
        loadManager = new LoadManager();
        satelliteManager = new SatelliteManager();
        System.out.println("LoadManager Starting: ");
       
        try {
            // read server properties
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
            // create server socket
            this.serverSock = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println("Failed to create ServerSocket: " + e);
        }
    }

    public void run() {
        
        // server loop; listening...
        while (true) 
        {
            try {
                // detect clients
                Socket clientSocket = serverSock.accept();
                // when a request comes in, a ServerThread object is spawned
                ServerThread client = new ServerThread(clientSocket);
                client.start();
            } catch (IOException e) {
                System.err.println("Failed to create client Socket: " + e);
            }
        }
    
    }

    // objects of this helper class communicate with satellites or clients
    private class ServerThread extends Thread {

        Socket client = null;
        ObjectInputStream readFromClient = null;
        ObjectOutputStream writeToClient = null;
        Message message = null;

        private ServerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            // set up object streams and read message
            // ...
            try {
                // get input and output streams for the client connection
                writeToClient = new ObjectOutputStream(client.getOutputStream());
                readFromClient = new ObjectInputStream(client.getInputStream());
                
                // read message from client
                message = (Message) readFromClient.readObject(); 
                
            } catch (IOException e) {
                System.err.println("Failed to create client socket IO streams: " + e);
            } 
            
            catch (ClassNotFoundException e) {
                System.err.println("Failed to create client socket IO streams: " + e);
            } 

            
            // process message if client connection is a satellite
            switch (message.getType()) {
                case REGISTER_SATELLITE:
                    
                    // register satellite
                    synchronized (Server.satelliteManager) {
                        // register satellite with satellite manager
                        Server.satelliteManager.registerSatellite((ConnectivityInfo) message.getContent());
                        
                    }

                    // add satellite to loadManager
                    synchronized (Server.loadManager) {
                        Server.loadManager.satelliteAdded(((ConnectivityInfo) message.getContent()).getName());
                    }

                    break;

                case JOB_REQUEST:
                    System.err.println("\n[ServerThread.run] Received job request");
                    
                    String satelliteName = null;
                    ConnectivityInfo satelliteInfo = null;
                    
                    synchronized (Server.loadManager) {
                        // get next satellite from load manager
                        try {
                            // get next satellite to send to
                            satelliteName = Server.loadManager.nextSatellite();
                        } catch (Exception e) {
                            System.err.println("Failed to get next satellite: " + e);
                        }
                        
                        // get connectivity info for next satellite from satellite manager
                        satelliteInfo = Server.satelliteManager.getSatelliteFromName(satelliteName);
                    }

                    Socket satellite = null;
                    ObjectInputStream readFromSat = null;
                    ObjectOutputStream writeToSat = null;
                    
                    
                    // connect to satellite
                    try {
                        // connect to satellite server
                        satellite = new Socket(satelliteInfo.getHost(), satelliteInfo.getPort());
                        
                        // capture input and output streams from connection
                        readFromSat = new ObjectInputStream(satellite.getInputStream());
                        writeToSat =  new ObjectOutputStream(satellite.getOutputStream());
                        
                        // forward message from client to satellite server
                        writeToSat.writeObject(message);
                        
                        // recieve result from satellite server
                        message = (Message) readFromSat.readObject();
                        
                        // send result back to client
                        writeToClient.writeObject(message.getContent());
                        
                        
                    } catch (IOException e) {
                        System.err.println("Failed to create connection to satellite: " + e);
                    } 
                    catch (ClassNotFoundException e) {
                        System.err.println("Failed to create client socket IO streams: " + e);
                    }

                    break;
                // if no message type provided, sends error message to console
                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }

    // main
    public static void main(String[] args) {
        // start the application server
        Server server = null;
        
        // if properties file provided, use it
        if(args.length == 1) {
            server = new Server(args[0]);
        } else {
            // if no properties file is is passed in, pass in default file
            server = new Server("../../config/Server.properties");
        }
        server.run();
    }
}
