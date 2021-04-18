package appserver.job.impl;

import java.util.Hashtable;

/**
 * Fibonacci Auxiliary Class
 * 
 * @author Group 5
 */
public class FibonacciAux {

    // number to compute
    Integer number = null; 
    
    /**
     * FibonacciAux Constructor 
     * 
     * @param number nth fib number to compute
     */
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
        
        // compute and return the nth term
        return recursiveFib(number, first, second);
        
    }
    
    /**
     * worker function to compute the nth fibonacci value
     * 
     * @param term nth term to calculate
     * @param first first sequence value
     * @param second second sequence value 
     * @return the nth element in the fibonacci sequence
     */
    private Long recursiveFib(Integer term, Long first, Long second) { 

        // compute when term to compute is 0
        if ( term == 0 ) {

            return first;
        }
        // compute last term 
        else if ( term == 1 ) {
            return second;
        }
        // recurse with the next values
        return recursiveFib( term - 1, second, first + second );
        
    }
    

}
