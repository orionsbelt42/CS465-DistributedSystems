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
    
    public Integer getResult() {
        int temp = 0;
        int first = 0;
        int second = 1;
        
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
