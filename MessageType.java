public enum MessageType {
    JOINING,
    JOINED,
    NOTE,
    LEAVING,
    NEW_PEER
};

class MsgHeader {
    public MessageType message_type;
}
class JoinMessage { // JOINING
    public MsgHeader id;
    public int peer_id;
};
class LeavingMessage { // LEAVING
    public MsgHeader id;
    public int peer_id;
};
class ChatMessage { // NOTE
    public MsgHeader id;
    public int chat_id;
    public String user;
    public String message;
};
class PeerMessage { // PEER
    public MsgHeader id;
    public String ip_address;
    public int peer_id;
};
class JoinAcceptMessage { //JOINED
    public MsgHeader id;
    public int chat_id;
};
