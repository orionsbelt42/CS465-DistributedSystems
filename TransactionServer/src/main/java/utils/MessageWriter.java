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
    
    public MessageWriter( int transactionID ) {
        this.ID = transactionID;
    }
    
    public byte[] readRequest(int accountID) {
        String msg = "READ_REQUEST: " + ID + ", " + accountID + "\n";
        return msg.getBytes();
    }
    
    public byte[] readResponse(int accountID, int balance) {
        String msg = "READ_RESPONSE: " + ID + ", " + accountID + ", " + balance + "\n";
        return msg.getBytes();
    }
    
    public byte[] writeRequest(int accountID, int amount) {
        String msg = "WRITE_REQUEST: " + ID + ", " + accountID + ", " + amount + "\n";
        return msg.getBytes();
    }
    
    public byte[] writeResponse(int accountID, int balance) {
        String msg = "WRITE_RESPONSE: " + ID + ", " + accountID + ", " + balance + "\n";
        return msg.getBytes();
    }
    
    public byte[] closeTransaction() {
        return "CLOSE_TRANSACTION: null\n".getBytes();
    } 
    
    public byte[] transactionResponse(ArrayList<Account> accounts) {
        String buffer = "TRANSACTION_RESPONSE: " + ID;
        int index;
        int size = accounts.size();
        
        if (size > 0) {
            buffer += ", ";
        }
        else {
            buffer += "\n";
        }
        
        for (index = 0; index < size; index++) {
            buffer += accounts.get(index);
            
            if (index < size - 1) {
                buffer += ", ";
            }
            else {
                buffer += "\n";
            }
        }
        return buffer.getBytes();
    }
    
}
