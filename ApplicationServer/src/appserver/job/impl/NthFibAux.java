package appserver.job.impl;

/**
 * 
 * 
 * @author
 */
public class NthFibAux {
    
    Integer number = null;
    
    public NthFibAux(Integer number) {
        this.number = number;
    }
    
    public Long getResult() {
        long temp = 0;
        long first = 0;
        long second = 1;
        
        int iter;
        
        for ( iter = 0; iter < number; iter++ )
        {
            temp = first + second;
            first = second;
            second = temp;
        }
        
        return first;
    }
    

}
