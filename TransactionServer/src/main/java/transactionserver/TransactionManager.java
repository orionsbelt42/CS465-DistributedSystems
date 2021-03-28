package transactionserver;

import java.net.*;
import java.util.*;
import java.io.*;
import utils.*;

public class TransactionManager
{
    static ArrayList<Transaction> transactionList;
    
    TransID lastID = new TransID();
    
    public TransactionManager() {
        // what do we do here?
    }
    
    public class TransactionManagerWorker implements Runnable {
        
        
        @Override
        public void run() {
            // recv and verify first message
           
            // create transaction object
            
            // assign id to transaction object
            
            // add transaction to list of transactions held by manager
            
            // return transaction id to clientAPI 
            
            // loop over next messages 
                // process message (big switch statement)
                
                // handle read/write requests through AccountManager
            
        }
        
    }
    public TransactionManager() {
        this.transactionList = new ArrayList<Transaction>();
    }
    
    // not sure what else it does

}