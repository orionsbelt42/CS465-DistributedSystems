/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactionclient;

import java.util.ArrayList;
import java.util.Random;
import transactionserver.AccountManager;
import utils.PropertyHandler;

public class Proxy implements Runnable{
    PropertyHandler configData;
        
    public Proxy(PropertyHandler configData){
        this.configData = configData;
    }
        
    @Override
    public void run() {
            
        // initialize API object for the client
        TransactionAPI transaction = new TransactionAPI(configData);

        ArrayList<Integer> accountIDs = transaction.openTransaction();

        Random rand = new Random();
        
        int writeResult;

        // pick two random accounts
        int sourceAcctIdx = rand.nextInt(accountIDs.size());
        int destAcctIdx = rand.nextInt(accountIDs.size());
        
        // pick random int value to transfer 
        int transferAmount = rand.nextInt(100);

        while (sourceAcctIdx == destAcctIdx){
            destAcctIdx = rand.nextInt(accountIDs.size());
        }

        // use API to request transfer
        int sourceBal = transaction.read(accountIDs.get(sourceAcctIdx));
        sourceBal -= transferAmount;
        writeResult = transaction.write(accountIDs.get(sourceAcctIdx), sourceBal);
        
        if (writeResult != AccountManager.DEADLOCK) {
            int destBal = transaction.read(accountIDs.get(destAcctIdx));
            destBal += transferAmount;
            writeResult = transaction.write(accountIDs.get(destAcctIdx), destBal);
            
            if (writeResult == AccountManager.DEADLOCK) {
                System.out.println("Transaction #" + transaction.getTransID() + " Experienced Deadlock");
                transaction.close();
            }
            else {
                transaction.closeTransaction();
            }
        }
        
        
    }
}