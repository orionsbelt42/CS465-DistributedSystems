/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactionclient;

import utils.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author CS 465 group 5
 */
public class TransactionAPI {
    
    final int SUCCESS_FLAG = 1;
    final int FAILURE_FLAG = -1;
    
    // stores socket connection and streams to and from server
    private Socket connection;
    private InputStream fromServer;
    private OutputStream toServer;
    
    // a PropertyHandler object created from a properties file
    private PropertyHandler config;
    
    // boolean checks to make sure essential variables are set before used
    private boolean connectedToServer = false;
    private boolean hasHostAddr = false;
    
    // connection address information
    private String hostname;
    private int port;
    
    private MessageWriter writer;
    private MessageReader reader;
    
    int transactionID;
    
    
    
    /**
     * A constructor using the properties file
     * 
     * @param properties the file data from the properties file
     */
    public TransactionAPI(PropertyHandler properties) {
        this.config = properties;
        this.hostname = properties.getProperty("SERVER_IP");
        this.port = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        this.reader = new MessageReader(true);
        
        hasHostAddr = true;
        
        // might just connect here 
    }
    
    /**
     * A more direct constructor
     * 
     * @param hostname server hostname or IP address
     * @param portNumber port server is listening to 
     */
    public TransactionAPI(String hostname, int portNumber) {
        this.hostname = hostname;
        this.port = portNumber;
        this.reader = new MessageReader();
        
        hasHostAddr = true;
    }
    
    /**
     * function to open the connection to the server
     * assumes that connection information is preset
     * 
     * @return operation success or failure
     */
    public int connect() {
        // setup connection to server
        if ( !hasHostAddr ) {
            return FAILURE_FLAG;
        }

        return connect(hostname, port);
    }
    
    /**
     * function to open the connection to the server
     * 
     * @param hostName server hostname or IP address
     * @param portNumber port number server is listening on
     * @return operation success or failure
     */
    private int connect(String hostName, int portNumber) {
        try
        {
            // create the socket connection
            connection = new Socket(hostName, portNumber); // connect to socket

            // get and store input and output streams from socket connection
            fromServer = connection.getInputStream(); // stream from server
            toServer = connection.getOutputStream(); // stream to server
            
            // let later processes know connection was successful 
            connectedToServer = true;
            
            // return success outcome
            return SUCCESS_FLAG;
        }
        catch (UnknownHostException e) // deal with any errors
        {
            System.out.println("Connection Failed: Unknown Host [" + hostname + "]");
            // return failure outcome
            return FAILURE_FLAG;
        }
        catch (IOException e)
        {
            System.out.println("Couldn't connect to host [" + hostname + ":" + port + "]");
            // return failure outcome
            return FAILURE_FLAG;
        }
    }
    
    /**
     * function to close the connection to the server
     * 
     * @return operation success or failure
     */
    public int close() {
        try {
            // try to close the socket connection
            connection.close();
            
            // return success outcome
            return SUCCESS_FLAG;
        } catch (IOException e) {
            System.out.println("Couldn't close connection to host [" + hostname + ":" + port + "]");
            // return failure outcome
            return FAILURE_FLAG;
        }
    }
    
    /**
     * primitive function to send simple text to a server
     * (might change)
     * 
     * @param MSG utf-8 encoded string to send
     */
    private void send(byte[] MSG) {
        try {
            // convert message to bytes and send to server
            this.toServer.write(MSG);
        } catch (IOException e) {
            System.out.println("Couldn't send message to host [" + hostname + ":" + port + "]");
        }
        
    }
    
    /**
     * function to listen for server messages
     * @return 
     */
    private String recv() {
        String buffer = "";
        char current;
        try {
            current = (char)fromServer.read();
            while (current != '\n') { // loop while the line has not ended
                buffer += current;
                current = (char)fromServer.read();
            }
        } catch (IOException e) {
            System.out.println("Error while reading from socket");
        }
        
        return buffer;
    }
 
    /**
     * API function that requests the start of a transaction from the server
     * 
     * @return the transaction id of the created transaction
     */
    public ArrayList<Integer> openTransaction() {
        
        ArrayList<String> response;
        ArrayList<Integer> converted = new ArrayList<Integer>();
        
        // connect to server 
            // function: connect
        connect();
            
        // send initial message/transaction start request
            // Create and send OPEN_TRANSACTION message to server
        send("OPEN_TRANSACTION: null\n".getBytes());
        
        // listen for the transaction id response from server
        String recieved = recv();
        
        // convert the message to an object
        response = reader.parseMessage(recieved);
        
        // get the transaction id
        transactionID = Integer.parseInt(response.get(1));
        
        writer = new MessageWriter(transactionID, true); // get new writer object
        TransactionClient.log.write("transaction #" + transactionID + " started");
        response.remove(0);
        response.remove(0);
        
        // parse available accounts
        for (String account:response) {
            converted.add(Integer.parseInt(account));
        }
        // store and return the transaction info recieved from the server
        return converted; 
    }
    
    
    /**
     * API function that requests the termination of a transaction on the server
     * 
     * @return the outcome of the request
     */
    public int closeTransaction() {
        // Create and send CLOSE_TRANSACTION message to server
        send(writer.closeTransaction());
        TransactionClient.log.write("transaction #" + transactionID + " finished");
        close();
        
        return 0; // temp return stub
    }
    
    /**
     * lower level API call to read account data off of server
     * 
     * @param account
     * @return the outcome of the request
     */
    public int read(int account) {
        // Create and send READ_REQUEST message to server
 
        send(writer.readRequest(account));
            
        return Integer.parseInt(reader.parseMessage(recv()).get(2));
        
        // not sure if server will send response so go off no error?
    }
    
    /**
     * lower level API call to write data to an account on the server
     * 
     * @param account the account the API is trying to write to
     * @param amount the amount to write to that account 
     * @return the outcome of the request
     */
    public int write(int account, int amount) {
        ArrayList<String> response;
        
        // Create and send WRITE_REQUEST message to server
        send(writer.writeRequest(account, amount));
        
        response = reader.parseMessage(recv());
        // not sure if server will send response so go off no error?

        return Integer.parseInt(response.get(3));
    }
    
    /**
     * Getter method for current transaction id
     * 
     * @return transaction id
     */
    public int getTransID() {
        return transactionID;
    }
}
