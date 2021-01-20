import java.io.*;
import java.net.*;

class Client {
 
    public static void main(String args[]) throws IOException {
        Socket socket = null;
        String str = null;
        BufferedReader buffread = null;
        DataOutputStream dos = null;
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        try {
            socket = new Socket("localhost", 1234);
            buffread = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException unknownHostExc) {
            System.out.println("Unknown Host");
            System.exit(0);
        }
        System.out.println("To start the dialog type the message in this client window \n Type quit to end");
        while (true) {
            str = userInput.readLine();
            dos.writeBytes(str);
            dos.write(13);
            dos.write(10);
            dos.flush();
            String line, lettersOnly;
            line = buffread.readLine();
			lettersOnly = line.replaceAll("[^a-zA-Z]","");
			if (lettersOnly.matches("(?i)quit")) {
                break;
            }
            System.out.println("From server, Echo: " + lettersOnly);
        }
        buffread.close();
        dos.close();
        socket.close();
    }
}