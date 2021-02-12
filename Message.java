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
    
    public Message(int senderIDEntry, MessageType type, String note){
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
