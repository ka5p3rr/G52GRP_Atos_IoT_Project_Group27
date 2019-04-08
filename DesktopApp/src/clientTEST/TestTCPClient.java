package clientTEST;
import java.net.*;
import java.io.*;

/**
 * TCP IP Client that tests the connection to server. It connects every second to retrieve the data from server.
 */
public class TestTCPClient {

    /**
     * Main function with the main loop.
     * @param args command line arguments
     */
	public static void main (String[] args) {
        // IP address and port number of the server
        String serverIP = "100.74.92.19";
        int serverPort = 7896;

        // loop forever
        while (true) {
            Socket socket = null;
            System.out.println("Connect to " + serverIP + ":" + serverPort);

            try {
                socket = new Socket(serverIP, serverPort);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                // send out
                String message = "Desktop App Test Client";
                System.out.println("Send:     " + message);
                out.writeUTF(message);

                // receive back
                String data = in.readUTF();
                System.out.println("Received: " + data);

            } catch (UnknownHostException e) {
                System.out.println("Socket:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO:" + e.getMessage());
            }

            // even when an exception is thrown close the socket
            finally {
                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Close:" + e.getMessage());
                    }
            }

            // wait for a second
            try {
                Thread.sleep(1000);
            } catch (
                    InterruptedException e) {e.printStackTrace();
            }
        }
	}
}
