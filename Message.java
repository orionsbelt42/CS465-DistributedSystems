import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
    The message to send in a Peer2Peer chat group. When class is initialized,
    the "note" parameter is intended to be blank when a client is joining,
    has joined, or is leaving the chat. All other times, the "note" parameter
    should be filled with the intended message.
*/
public class Message {
    private int senderID;
    private MessageType messageID;
    private String body;
    private int chatPort = 4200;
	// host should be passed in?
	String host = "some_host_ip_as_received_in_participant";
    public Boolean Message(int senderIDEntry, MessageType type, String note) throws IOException
    {
        try {
            //System.out.println("Send " + note + " to " + host + ":" + port);
            byte[] data = note.getBytes();
            DatagramSocket outsock = new DatagramSocket();
            DatagramPacket outpack = new DatagramPacket(data, data.length, InetAddress.getByName(host), chatPort);
            outsock.send(outpack);
            System.out.print("sent " + note);
        } catch (IOException ioerr) {
            return false;
        }
        senderID = senderIDEntry;
        messageID = type;

        if(messageID == MessageType.JOINING){
            body = "Client is attempting to join Peer2Peer chat group";
        }
        else if( messageID == MessageType.JOINED){
            body = "Client has successfully joined Peer2Peer chat group";
        }
        else if( messageID == MessageType.NOTE) {
            body = note;
        }
        else {
            body = "Client has left the Peer2Peer chat group";
        }

		return true;
    }

    public int getSenderID(){
        return senderID;
    }

    public MessageType getMessageType() {
        return messageID;
    }

    public String getMessage(){
        return body;
    }
}