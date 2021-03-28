/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 *
 * @author asfre
 */
public class FriendlyDict {
    
    
    Dictionary dict;
    
    public FriendlyDict(){
        dict = new Hashtable();
    }

    
    public FriendlyDict(FriendlyDict copyDict){
        this();
    }
    
    public FriendlyDict(Dictionary copyDict){
        this();
    }
    
    public String get(String key){
        dict.get(key);
    }
    
    public void put( ){
        dict.put(key);
    }
    
    
}
