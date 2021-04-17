/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.util.*;

public class MessageReader {
    boolean log;
    
    /**
     * default constructor
     */
    public MessageReader() {
        log = false;
    }
    
    /**
     * constructor with log value
     * 
     * @param logValue option to log to file
     */
    public MessageReader(boolean logValue) {
        log = false;//logValue;
    }
    
    /**
     * convert byte[] message to data object
     * 
     * @param message byte[] from recv method
     * @return the message and args as an arraylist
     */
    public ArrayList<String> parseMessage(byte[] message) {
        return parseMessage(new String(message));
    }
    
    /**
     * convert String message to data object
     * 
     * @param message String from recv method
     * @return the message and args as an arraylist
     */
    public ArrayList<String> parseMessage(String message) {
        logMsg(message);
        // parse and split message into type and args
        ArrayList<String> arguments = new ArrayList<String>();
        message = message.replaceAll("\\s", "");
        String[] temp = message.split(":", 0);
        arguments.add(0, temp[0]);
        for (String arg: temp[1].split(",", 0)) {
            arguments.add(arg);
        }
        
        return arguments;
    }
    
    /**
     * set the log value
     * 
     * @param value new log value
     */
    public void setLog(boolean value){
        log = value;
    }
    
    /**
     * log message function
     * 
     * @param message message to print
     */
    public void logMsg(String message) {
        if (log) {
            System.out.println("Recieved Message: " + message);
        }
    }
}
