package utils;

import java.io.*;
import java.util.*;

public class SystemLog {
    public ArrayList<String> history; // transaction history
    public String logFile; // filename for log file
    public String logTo; // op code
    private BufferedWriter log; // file oobject
    
    /**
     * default constructor
     */
    public SystemLog() {
        this("system.log", "both"); 
    }
    
    /**
     * secondary constructor
     * 
     * @param filename filename to log to
     */
    public SystemLog(String filename) {
        this(filename, "both");
    }
    
    /**
     * another constructor for log options
     * 
     * @param filename file to write output to
     * @param logCode operation code
     */
    public SystemLog(String filename, String logCode) {
        logFile = filename;
        logTo = logCode.toLowerCase();
        history = new ArrayList<String>();
        
        log = openFile(filename);
        //this.write("test");
    }
    
    /**
     * Function to open log file for writing 
     * 
     * @param filename file to open
     * @return a Buffered writer object
     */
    private BufferedWriter openFile(String filename) {
        try {
            FileWriter file = new FileWriter(filename);
            BufferedWriter fileWrite = new BufferedWriter(file);
            return fileWrite;
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        
        return null;
    }
    
    /**
     * writes a string to the file
     * 
     * @param msg string to write
     */
    public void write(String msg) {
        if (!logTo.equals("file")) {
            System.out.println(msg);
        }
        
        if (!logTo.equals("moniter")) {
            try {
                log.write(msg + "\n");
            } catch (IOException e){
            }
        }
    }
    
    /**
     * function to write multiple lines 
     * 
     * @param messageList array list of lines
     */
    public void write(ArrayList<String> messageList) {

        try {
            for (String msg: messageList){
                log.write(msg+"\n");
            }
        } catch (IOException e){
        } 
    }
    
    /**
     * function to close file object
     */
    public void close(){
        
        try {
            log.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
    
    
}
