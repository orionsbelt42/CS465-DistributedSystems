package utils;

import java.io.*;
import java.util.*;

public class SystemLog {
    public ArrayList<String> history;
    public String logFile;
    public String logTo;
    private FileWriter log;
    
    public SystemLog() {
        this("system.log", "both");
        
        
        
    }
    
    public SystemLog(String filename) {
        this(filename, "both");
    }
    
    public SystemLog(String filename, String logCode) {
        logFile = filename;
        logTo = logCode.toLowerCase();
        history = new ArrayList<String>();
        
        log = openFile(filename);
        //this.write("test");
    }
    
    private FileWriter openFile(String filename) {
        try {
            FileWriter file = new FileWriter(filename);
            file.write("Files in Java might be tricky, but it is fun enough!");
            return file;
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        
        return null;
    }
    
    public void write(String msg) {
        if (!logTo.equals("file")) {
            System.out.println(msg);
        }
        
        if (!logTo.equals("moniter")) {
            try {
                log.write(msg);
            } catch (IOException e){
            }
        }
       
    }
    
    public void close(){
        
        try {
            log.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
    
    
}
