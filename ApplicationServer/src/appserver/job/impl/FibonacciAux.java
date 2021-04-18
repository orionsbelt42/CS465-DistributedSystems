package appserver.job.impl;

import java.util.Hashtable;

/**
 * Fibonacci Auxiliary Class
 * 
 * @author Group 5
 */
public class FibonacciAux {
    // store fibonacci values already computed
    static Hashtable<Integer, Long> knownFib = new Hashtable(92);
    
    // number to compute
    Integer number = null; 
    
    // default constructor
    public FibonacciAux(Integer number) {
        this.number = number;
    }
    
    /**
     * get the nth term of the fibonacci sequence
     * 
     * @return the computed nth term
     */
    public Long getResult() {
        
        long first = 0; // first value in the sequence
        long second = 1; // second value in the sequence
        
        int length = knownFib.size(); // get size of know storage
        
        
        // if the number has been computed before
        if ( knownFib.containsKey(number) ) {
            // return the pre-computed result from storage
            return knownFib.get(number);
        }
        
        // if some numbers have already been computed
        // but not the number we want
        else if ( knownFib.size() > 1 ) {
            
            // second to last number already computed
            first =  knownFib.get(length-1); 
            
            // last number already computed
            second = knownFib.get(length);
            
            // call fibonacci function starting at last computed values
            return recursiveFib(number-length+1, first, second, length);
        }
        // assume no work has been done before
        else {
            //  so start with the first sequence values
            return recursiveFib(number, first, second, 1);
        }
        
        
    }
    
    /**
     * worker function to compute the nth fibonacci value
     * 
     * @param term nth term to calculate
     * @param first first sequence value
     * @param second second sequence value
     * @param nth current number in the sequence being calculated  
     * @return the nth element in the fibonacci sequence
     */
    private Long recursiveFib(Integer term, Long first, Long second, int nth) { 
        // check to see if value is not computed
        if ( !knownFib.containsKey(nth) ) {
            // add value to computed storage
            knownFib.put( nth, second );
        }
        
        // compute when term to compute is 0
        if ( term == 0 ) {

            return first;
        }
        // compute last term 
        else if ( term == 1 ) {
            return second;
        }
        // recurse with the next values
        return recursiveFib( term - 1, second, first + second, nth + 1 );
        
    }
    

}
