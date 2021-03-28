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
import transactionserver.TransID;

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
    
    /**
     * A constructor using the properties file
     * 
     * @param properties the file data from the properties file
     */
    public TransactionAPI(PropertyHandler properties) {
        this.config = properties;
        this.hostname = properties.getProperty("HOST");
        this.port = Integer.parseInt(properties.getProperty("PORT"));
        
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
            System.err.println("Connection Failed: Unknown Host [" + hostname + "]");
            // return failure outcome
            return FAILURE_FLAG;
        }
        catch (IOException e)
        {
            System.err.println("Couldn't connect to host [" + hostname + ":" + port + "]");
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
            System.err.println("Couldn't close connection to host [" + hostname + ":" + port + "]");
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
    public void send(String MSG) {
        try {
            // convert message to bytes and send to server
            this.toServer.write(MSG.getBytes());
        } catch (IOException e) {
            System.err.println("Couldn't send message to host [" + hostname + ":" + port + "]");
        }
        
    }
 
    /**
     * API function that requests the start of a transaction from the server
     * 
     * @return the transaction id of the created transaction
     */
    public TransID openTransaction() {
        // connect to server 
            // function: connect
            
        // send initial message/transaction start request
            // Create and send OPEN_TRANSACTION message to server
        
        // listen for the transaction id response from server
        
        // store and return the transaction id recieved from the server
        return null; // temp return stub
    }
    
    
    /**
     * API function that requests the termination of a transaction on the server
     * 
     * @return the outcome of the request
     */
    public int closeTransaction() {
        // Create and send CLOSE_TRANSACTION message to server
        
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
        
        // not sure if server will send response so go off no error?
        return 0; // temp return stub
    }
    
    /**
     * lower level API call to write data to an account on the server
     * 
     * @param account the account the API is trying to write to
     * @param amount the amount to write to that account 
     * @return the outcome of the request
     */
    public int write(int account, int amount) {
        // Create and send WRITE_REQUEST message to server
        
        // not sure if server will send response so go off no error?
        return 0; // temp return stub
    }
}