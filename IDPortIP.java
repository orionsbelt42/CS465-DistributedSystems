/*
    holds the IP value and the ID number of a specific client
*/
public class IDPortIP {
    private int id;
    private String ip;
    private int port;

    public void setID(int idEntry){
        id = idEntry;
    }

    public void setPort(int portEntry){
        port = portEntry;
    }

    public void setIP(String ipEntry){
        ip = ipEntry;
    }

    public int getID(){
        return id;
    }

    public int getPort(){
        return port;
    }

    public String getIP(){
        return ip;
    }
}