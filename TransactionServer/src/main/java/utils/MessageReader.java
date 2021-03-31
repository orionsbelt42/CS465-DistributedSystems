/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.util.*;

public class MessageReader {
    boolean log;
    
    public MessageReader() {
        log = false;
    }
    
    public MessageReader(boolean logValue) {
        log = false;//logValue;
    }
    
    public ArrayList<String> parseMessage(byte[] message) {
        return parseMessage(new String(message));
    }
    
    public ArrayList<String> parseMessage(String message) {
        logMsg(message);
        
        ArrayList<String> arguments = new ArrayList<String>();
        message = message.replaceAll("\\s", "");
        String[] temp = message.split(":", 0);
        arguments.add(0, temp[0]);
        for (String arg: temp[1].split(",", 0)) {
            arguments.add(arg);
        }
        
        return arguments;
    }
    
    public void setLog(boolean value){
        log = value;
    }
    
    public void logMsg(String message) {
        if (log) {
            System.out.println("Recieved Message: " + message);
        }
    }
}
