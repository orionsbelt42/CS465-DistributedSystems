/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author asfre
 */
public class Properties {
    File propertyFile = null;
    Dictionary propertyDict = new Hashtable();
    
    /**
     * Copy constructor 
     * @param toCopy 
     * @throws java.io.FileNotFoundException
     */
    public Properties(Properties toCopy) {
        if (toCopy != null)
        {
            this.propertyFile = toCopy.propertyFile;
        }
        
        this.propertyFile = null;
    }
    
    public void load(InputStream fileData) {
        /*
        int fileSize = fileData.available();
        byte[] inputBuffer = new byte[fileSize];
        
        System.out.println("FileSize: " + fileSize);
        fileData.read(inputBuffer);
        */
        String buffer;
        String[] argList = new String[2];
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileData));
        char currentChar;
        while(reader.ready()) {
            buffer = reader.readLine();
            
            if (!(buffer.equals("") || buffer.charAt(0) == '#')) {
                buffer = buffer.replaceAll("\\s", "");
                argList = buffer.split("=", 2);
                
                propertyDict.put(argList[0], argList[1]);
            }
            
        }
        
    }
    
    
    public String getProperty(String key) {
        String cleanKey = key.replaceAll("\\s", "");
        
        if (propertyDict.containsKey(cleanKey)){
            return (String) propertyDict.get(key);
        }
        
        return "PROPERTY_NOT_FOUND+ERROR";
        
    }
    
    public void showProperties(){
        System.out.println("Properties: " + propertyDict);
    }
    
    @Override
    public String toString(){
        return propertyDict.toString();
    }
}
