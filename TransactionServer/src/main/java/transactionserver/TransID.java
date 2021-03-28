package transactionserver;

public class TransID
{
    private int transID;

    public TransID()
    {
        transID = 0;
    }
    
    
    public TransID(int idNum)
    {
        transID = idNum;
    }

    public TransID(TransID lastTransID)
    {
        transID = lastTransID.getID() + 1;
    }

    public int getID()
    {
        return transID;
    }

    public void setID(int idNum)
    {
        transID = idNum;
    }
}