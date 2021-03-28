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
 * @author asfre
 */
public class TransactionAPI {
    
    final int SUCCESS_FLAG = 1;
    final int FAILURE_FLAG = -1;
    private Socket connection;
    private InputStream fromServer;
    private OutputStream toServer;
    private PropertyHandler config;
    
    
    private boolean connectedToServer = false;
    private boolean hasHostAddr = false;
    
    
    private String hostname;
    private int port;
    
    public TransactionAPI(PropertyHandler properties) {
        this.config = properties;
        this.hostname = properties.getProperty("HOST");
        this.port = Integer.parseInt(properties.getProperty("PORT"));
        
        hasHostAddr = true;
    }
    
    public TransactionAPI(String hostname, int portNumber) {
        this.hostname = hostname;
        this.port = portNumber;
        
        hasHostAddr = true;
    }
    
    public int connect() {
        // setup connection to server
        if ( !hasHostAddr ) {
            return FAILURE_FLAG;
        }

        return connect(hostname, port);
    }
    
    private int connect(String hostName, int portNumber) {
        try
        {
            connection = new Socket(hostName, portNumber); // connect to socket

            fromServer = connection.getInputStream(); // stream from server
            toServer = connection.getOutputStream(); // stream to server
            
            connectedToServer = true;
            
            return SUCCESS_FLAG;
        }
        catch (UnknownHostException e) // deal with any errors
        {
            System.err.println("Connection Failed: Unknown Host [" + hostname + "]");
            return FAILURE_FLAG;
        }
        catch (IOException e)
        {
            System.err.println("Couldn't connect to host [" + hostname + ":" + port + "]");
            return FAILURE_FLAG;
        }
    }
    
    public int close() {
        try {
            connection.close();
            return SUCCESS_FLAG;
        } catch (IOException e) {
            System.err.println("Couldn't close connection to host [" + hostname + ":" + port + "]");
            return FAILURE_FLAG;
        }
    }
    
    public void send(String MSG) {
        try {
            this.toServer.write(MSG.getBytes());
        } catch (IOException e) {
            System.err.println("Couldn't send message to host [" + hostname + ":" + port + "]");
        }
        
    }
 
    public TransID openTransaction() {
    
    }
    
    public int closeTransaction() {
    
    }
    
    public int read(int account) {
        
    }
    
    public int write(int account, int amount) {
        
    }
}
