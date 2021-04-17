package appserver.satellite;

import appserver.job.Job;
import appserver.comm.ConnectivityInfo;
import appserver.job.UnknownToolException;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import static java.lang.Integer.valueOf;

import appserver.job.Tool;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.PropertyHandler;

/**
 * Class [Satellite] Instances of this class represent computing nodes that execute jobs by
 * calling the callback method of tool a implementation, loading the tool's code dynamically over a network
 * or locally from the cache, if a tool got executed before.
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Satellite extends Thread {

    private ConnectivityInfo satelliteInfo = new ConnectivityInfo();
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    private HTTPClassLoader classLoader = null;
    private Hashtable toolsCache = null;

    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) {

        // read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        try {
            File myfp = new File(satellitePropertiesFile);
            Scanner myScanner = new Scanner(myfp);
            while (myScanner.hasNextLine()) {
                String data = myScanner.nextLine();
                String values[] = data.split("\\t");
                if (values.length > 1)
                {
                    if (values[0].trim().equals("PORT"))
                    {
                        Integer port = valueOf(values[1]);
                        satelliteInfo.setPort(port);
                        System.out.println("satelliteInfo port " + satelliteInfo.getPort());
                    }
                    else if (values[0].trim().equals("NAME"))
                    {
                        satelliteInfo.setName(values[1]);
                        System.out.println("satelliteInfo name " + satelliteInfo.getName());
                    }
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        
        // read properties of the application server and populate serverInfo object
        // other than satellites, the as doesn't have a human-readable name, so leave it out
        try {
            File myfp = new File(serverPropertiesFile);
            Scanner myScanner = new Scanner(myfp);
            while (myScanner.hasNextLine()) {
                String data = myScanner.nextLine();
                String values[] = data.split("\\s=\\s");
                if (values.length > 1)
                {
                    if (values[0].trim().equals("PORT"))
                    {
                        int port = valueOf(values[1]);
                        serverInfo.setPort(port);
                        System.out.println("serverInfo port " + serverInfo.getPort());
                    }
                    else if (values[0].trim().equals("HOST"))
                    {
                        serverInfo.setHost(values[1]);
                        System.out.println("serverInfo host " + serverInfo.getHost());
                    }
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        
        // read properties of the code server and create class loader
        // -------------------
        String clhost = "";
        Integer clport = 0;
        try {
            File myfp = new File(classLoaderPropertiesFile);
            Scanner myScanner = new Scanner(myfp);
            while (myScanner.hasNextLine()) {
                String data = myScanner.nextLine();
                String values[] = data.split("\\s");
                if (values.length > 1)
                {
                    if (values[0].trim().equals("PORT"))
                    {
                        clport = valueOf(values[1]);
                        System.out.println("classLoader port " + clport);
                    }
                    else if (values[0].trim().equals("HOST"))
                    {
                        clhost = values[1];
                        System.out.println("classLoader host " + clhost);
                    }
                }
            }
            myScanner.close();
            classLoader = new HTTPClassLoader(clhost, clport);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // create tools cache
        // -------------------
        toolsCache = new Hashtable<String, Tool>();

    }

    @Override
    public void run() {
        // register this satellite with the SatelliteManager on the server
        // ---------------------------------------------------------------
        ObjectOutputStream writeToApp = null;
        Message msg = new Message(REGISTER_SATELLITE, satelliteInfo);
        // serverInfo
        try{
            Socket appSocket = new Socket(serverInfo.getHost(), serverInfo.getPort());
            writeToApp = new ObjectOutputStream(appSocket.getOutputStream());
            writeToApp.writeObject(msg);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // create server socket
        // ---------------------------------------------------------------
        try {
            ServerSocket serverSocket = new ServerSocket(satelliteInfo.getPort());

            // start taking job requests in a server loop
            // ---------------------------------------------------------------
            while (true)
            {
                System.out.println("Accepting client requests on port " + satelliteInfo.getPort());
                Socket clientSocket = serverSocket.accept();
                System.out.println("Received client request, creating SatelliteThread");
                SatelliteThread satthread = new SatelliteThread(clientSocket, this);
                satthread.run();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread {

        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        SatelliteThread(Socket jobRequest, Satellite satellite) {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() {
            // setting up object streams
            try {
                writeToNet = new ObjectOutputStream(jobRequest.getOutputStream());
                readFromNet = new ObjectInputStream(jobRequest.getInputStream());

                // reading message
                message = (Message) readFromNet.readObject();

                switch (message.getType()) {
                    case JOB_REQUEST:
                        // processing job request
                        Job job = (Job) message.getContent();
                        Tool tool = getToolObject(job.getToolName());
                        tool.go(job.getParameters());
                        break;

                    default:
                        System.err.println("[SatelliteThread.run] Warning: Message type not implemented");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (UnknownToolException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Aux method to get a tool object, given the fully qualified class string
     * If the tool has been used before, it is returned immediately out of the cache,
     * otherwise it is loaded dynamically
     */
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Tool toolObject = null;
        if (toolsCache.containsKey(toolClassString))
        {
            toolObject = (Tool) toolsCache.get(toolClassString);
        }
        else
        {
            Class toolClass = classLoader.findClass(toolClassString);
            toolObject = (Tool) toolClass.newInstance();
            toolsCache.put(toolClassString, toolObject);
        }

        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
        Satellite satellite = new Satellite(args[0], args[1], args[2]);
        satellite.run();
    }
}
