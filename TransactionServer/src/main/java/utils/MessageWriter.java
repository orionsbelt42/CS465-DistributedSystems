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
    
    public MessageWriter( int transactionID ) {
        this.ID = transactionID;
        this.log = false;
    }
    
    public MessageWriter( int transactionID, boolean logValue ) {
        this.ID = transactionID;
        this.log = false;//logValue;
    }
    
    public byte[] readRequest(int accountID) {
        String msg = "READ_REQUEST: " + ID + ", " + accountID + "\n";
        logMsg(msg);
        return msg.getBytes();
    }
    
    public byte[] readResponse(int accountID, int balance) {
        String msg = "READ_RESPONSE: " + ID + ", " + accountID + ", " + balance + "\n";
        logMsg(msg);
        return msg.getBytes();
    }
    
    public byte[] writeRequest(int accountID, int amount) {
        String msg = "WRITE_REQUEST: " + ID + ", " + accountID + ", " + amount + "\n";
        logMsg(msg);
        return msg.getBytes();
    }
    
    public byte[] writeResponse(int accountID, int balance) {
        String msg = "WRITE_RESPONSE: " + ID + ", " + accountID + ", " + balance + "\n";
        logMsg(msg);
        return msg.getBytes();
    }
    
    public byte[] closeTransaction() {
        logMsg("Sent Message: CLOSE_TRANSACTION: null\n");
        return "CLOSE_TRANSACTION: null\n".getBytes();
    } 
    
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
    
    public void setLog(boolean value){
        log = value;
    }
    
    public void logMsg(String msg){
        if (log) {
            System.out.print("Sent Message: " + msg);
        }
    }
    
}
