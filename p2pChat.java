package p2pChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

class ChatSender extends Thread {
    private int port;
    private String fromIP;
    private final static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private DatagramSocket datasock = null;
    private DatagramPacket datapack = null;

    public ChatSender(String ip, int port) throws Exception {
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
                // save off address to send out to

                // test sending out message
                sendMsg(msg, "localhost");
            }
        } catch (SocketException sockerr) {
            System.err.println("error (Socket Closed): " + sockerr.getMessage());
        } catch (IOException sockerr) {
            System.err.println("error: " + sockerr.getMessage());
        }
    }

    public boolean sendMsg(String msg, String host) throws IOException {
        try {
            System.out.println("Send " + msg + " to " + host + ":" + port);
            byte[] data = msg.getBytes();
            DatagramSocket outsock = new DatagramSocket();
            DatagramPacket outpack = new DatagramPacket(data, data.length, InetAddress.getByName(host), port);
            outsock.send(outpack);
            System.out.print("sent " + msg);
            return true;
        } catch (IOException ioerr) {
            return false;
        }
    }
}

class Main {
    public static void main(String args[]) throws Exception {
        ChatSender chatter = new ChatSender("localhost", 4200);
    }
}