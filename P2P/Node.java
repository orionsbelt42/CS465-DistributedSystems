import java.io.*;
import java.net.*;

public class Node {
    /* message structure

        "ACTION: [Join, Message, Update, Leave]" (required)
        "NAME: [name]" (optional)
        "ID: [number]" (optional)
        "SENDER: [ip] [port]" (required)
        "BODY: [data/null]" (required)
        "END: [true/false]" (required)
    */
    int id;
    String name;
    String hostName;
    int port;

    Node(){
        id = -1;
        name = "Unknown";
        hostName = "127.0.0.1";
        port = 8080;
    }

    Node(int newId, String newName, String newHostName, int newPortNumber){
        id = newId;
        name = newName;
        hostName = newHostName;
        port = newPortNumber;

    }


    public String toString(){
        return hostName + ":" + String.valueOf(port) + "," + name  ;
        //return "\n=================================\nID: " + String.valueOf(id) + "\nNAME: " + name + "\nADDRESS: " + hostName + ":" + String.valueOf(port) + "\n=================================\n";
    }

    public boolean equals(Node otherNode){
        return (this.hostName == otherNode.hostName)
                && (this.port == otherNode.port);
    }

}
