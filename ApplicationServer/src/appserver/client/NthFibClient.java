/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserver.client;

import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import appserver.job.Job;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import utils.PropertyHandler;

/**
 *
 * @author asfre
 */
public class NthFibClient {
    String host = null;
    int port;
    
    Properties properties;

    int number;
    public NthFibClient(String serverPropertiesFile, int number) {
        try {
            this.number = number;
            properties = new PropertyHandler(serverPropertiesFile);
            host = properties.getProperty("HOST");
            System.out.println("[NthFibClient.NthFibClient] Host: " + host);
            port = Integer.parseInt(properties.getProperty("PORT"));
            System.out.println("[NthFibClient.NthFibClient] Port: " + port);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void run() {
        try { 
            // connect to application server
            Socket server = new Socket(host, port);
            
            // signal class to use for calculations
            String classString = "appserver.job.impl.NthFib";
            
            // current number being cal
            
            // create job and job request message
            Job job = new Job(classString, new Integer(number));
            Message message = new Message(JOB_REQUEST, job);

            // sending job out to the application server in a message
            ObjectOutputStream writeToNet = new ObjectOutputStream(server.getOutputStream());
            writeToNet.writeObject(message);

            // reading result back in from application server
            // for simplicity, the result is not encapsulated in a message
            ObjectInputStream readFromNet = new ObjectInputStream(server.getInputStream());
            Integer result = (Integer) readFromNet.readObject();
            System.out.println("RESULT: " + result);
            
            
        } catch (Exception ex) {
            System.err.println("[NthFibClient.run] Error occurred");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        NthFibClient client = null;
        int number;
        String filename;
        
        if(args.length == 1) {
                filename = args[0];
        } else {
            filename =  "../../config/Server.properties";
        }
        
        for ( number = 1; number < 49; number++ )
        {
            client = new NthFibClient(filename, number);
            client.run();
        }
    }
}