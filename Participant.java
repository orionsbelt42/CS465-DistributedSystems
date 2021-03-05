import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;



public class Participant extends Thread {
    private int port;
    private String fromIP;
    private final static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private DatagramSocket datasock = null;
    private DatagramPacket datapack = null;

    public void Participant(String ip, int port) throws Exception {
        fromIP = ip;
        this.port = port;
        System.out.println("chat: IP address: " + fromIP + " port " + this.port);
        start();
    }

    public void run() {
        try {
            // open DatagramSocket to receive
            datasock = new DatagramSocket(port);
            // loop forever reading datagrams from the DatagramSocket
            while (true) {
                byte[] buffer = new byte[5000];
                datapack = new DatagramPacket(buffer, buffer.length);
                datasock.receive(datapack);
                String msg = new String(datapack.getData(), 0, datapack.getLength());

                System.out.print(msg);
                System.out.println("UDP length " + msg.length() + " from " + datapack.getAddress() + " rcvd: " + msg);
                MsgHeader msgH = MsgHeader.class.cast(msg);
                switch (msgH.message_type)
                {
                    case JOINING:
                        // call join with JoinMessage.class.cast(msg) and send back
                        // JoinAcceptMessage with a chat id, some id that you can use to authorize a message
                        // as coming from a joined client
                        break;
                    case JOINED:
                        // call addParticipant with JoinAcceptMessage.class.cast(msg) and store chat_id
                        // populate in outgoing chat messages, store client info, send PeerMessage to all other
                        // clients so they can add to their lists
                        break;
                    case NOTE:
                        // call displayMessage with ChatMessage.class.cast(msg), check chat_id and proceed
                        break;
                    case LEAVING:
                        // call removeParticipant with LeavingMessage.class.cast(msg)
                        // remove client info
                        break;
                    case NEW_PEER:
                        // call newPeer with PeerMessage.class.cast(msg) and store client info
                        break;
                }
            }
        } catch (SocketException e) {
            System.err.println("error (Socket Closed): " + e.getMessage());
        } catch (IOException e) {
            System.err.println("error: " + e.getMessage());
        }
    }
    private ArrayList<IDPortIP> participantList;

    public Boolean join(int PeerIP, int PeerPort){

        // return statement placement holder
        return false;
    }

    public Boolean addParticipant(int ParticipantID){

  //      ChatNode.ParticipantList.append(ParticipantID);
        return false;
    }

    public Boolean removeParticipant(int ParticipantID){

   //     ChatNode.ParticipantList.remove(ParticipantID);
        return false;
    }

    public void sendMessage(int SenderID, Message note){
	
    }

    public void displayMessage(Message note){
		println(note);
    }

    public void leave(){

    }
}