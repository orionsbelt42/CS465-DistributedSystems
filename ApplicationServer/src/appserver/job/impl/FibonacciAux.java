package appserver.job.impl;

import java.util.ArrayList;

/**
 * 
 * 
 * @author
 */
public class FibonacciAux {
    
    ArrayList<Long> known = new ArrayList();
    Integer number = null;
    
    public FibonacciAux(Integer number) {
        this.number = number;
    }
    
    public Long getResult() {
        long temp = 0;
        long first = 0;
        long second = 1;
        
        if ( number < known.size() && number > 0 ) {
            return known.get(number-1);
        }
        
        return first;
    }
    
    private recursiveFib(Long first, Long second){
        
    }
    

}
