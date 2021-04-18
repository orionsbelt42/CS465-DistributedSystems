/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserver.job.impl;

import appserver.job.Tool;
/**
 * Fibonacci Tool class
 * 
 * @author Group 5
 */
public class Fibonacci implements Tool{
    FibonacciAux helper = null;
    /**
     * calls correct aux function to do calculation
     * 
     * @param parameters an Object holding an Integer
     * @return the result of the calculation 
     */
    @Override
    public Object go(Object parameters) {
        // call fibonacci aux function with unpacked Integer
        helper = new FibonacciAux((Integer) parameters);
        
        // return the result
        return helper.getResult();
    }
}
