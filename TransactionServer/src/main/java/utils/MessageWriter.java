/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.*;
import transactionserver.Account;

public class MessageWriter {
    int ID;
    boolean log;
    
    /** 
     * constructor using a given id
     * 
     * @param transactionID the id to use
     */
    public MessageWriter( int transactionID ) {
        this.ID = transactionID;
        this.log = false;
    }
    
    /**
     * constructor using a given id and log value
     * 
     * @param transactionID the id to use
     * @param logValue log option
     */
    public MessageWriter( int transactionID, boolean logValue ) {
        this.ID = transactionID;
        this.log = false;//logValue;
    }
    
    /**
     * generate new read request
     * 
     * @param accountID account id for request
     * @return a byte[] containing the formatted message
     */
    public byte[] readRequest(int accountID) {
        String msg = "READ_REQUEST: " + ID + ", " + accountID + "\n";
        logMsg(msg);
        return msg.getBytes();
    }
    
    /**
     * generate new read response message to show results of read
     * 
     * @param accountID account id number
     * @param balance new balance
     * @return read response message
     */
    public byte[] readResponse(int accountID, int balance) {
        String msg = "READ_RESPONSE: " + ID + ", " + accountID + ", " + balance + "\n";
        logMsg(msg);
        return msg.getBytes();
    }
    
    /**
     * generate new read request message
     * 
     * @param accountID account id number
     * @param amount amount to write
     * @return write request message
     */
    public byte[] writeRequest(int accountID, int amount) {
        String msg = "WRITE_REQUEST: " + ID + ", " + accountID + ", " + amount + "\n";
        logMsg(msg);
        return msg.getBytes();
    }
    
    /**
     * generate new write response message
     * 
     * @param accountID account id number
     * @param balance balance written by server
     * @return write response message
     */
    public byte[] writeResponse(int accountID, int balance) {
        String msg = "WRITE_RESPONSE: " + ID + ", " + accountID + ", " + balance + "\n";
        logMsg(msg);
        return msg.getBytes();
    }
    
    /**
     * create close transaction message
     * 
     * @return close transaction message
     */
    public byte[] closeTransaction() {
        logMsg("Sent Message: CLOSE_TRANSACTION: null\n");
        return "CLOSE_TRANSACTION: null\n".getBytes();
    } 
    
    /**
     * create response message for open transaction response
     * 
     * @param accounts a list of account id numbers
     * @return message containing a list of active accounts
     */
    public byte[] transactionResponse(ArrayList<Account> accounts) {
        String buffer = "TRANSACTION_RESPONSE: " + ID;
        int index;
        int size = accounts.size();
        
        if (size > 0) {
            buffer += ", ";
        }
        
        for (index = 0; index < size; index++) {
            buffer += (accounts.get(index)).getID();
            
            if (index < size - 1) {
                buffer += ", ";
            }
        }
        
        buffer += "\n";
        logMsg(buffer);
        return buffer.getBytes();
    }
    
    /**
     *  create committed message
     * 
     * @return commit message
     */ 
    public byte[] committed() {
        return "TRANSACTION_COMMITED: null\n".getBytes();
    }
    
    /**
     * function the set log value
     * 
     * @param value new value
     */
    public void setLog(boolean value){
        log = value;
    }
    
    /**
     * function to print created message
     * 
     * @param msg 
     */
    public void logMsg(String msg){
        if (log) {
            System.out.print("Sent Message: " + msg);
        }
    }
    
}
