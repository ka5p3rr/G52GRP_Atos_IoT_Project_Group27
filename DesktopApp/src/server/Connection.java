package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Takes care of each connection to the Server as a new thread.
 */
public class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    /**
     * This is send back to the client.
     */
    private static String data = "0%";

    /**
     * Constructor
     * @param aClientSocket socket to use for each connection
     */
    Connection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream( clientSocket.getInputStream());
            out = new DataOutputStream( clientSocket.getOutputStream());
        } catch(IOException e) {e.printStackTrace();}
    }

    /**
     * Executes this class thread. Reads from the input data from socket and sends back the data.
     */
    public void run(){
        try {
            // always needs to read the input stream
            in.readUTF();
            // here do processing for what to sent to clients
            System.out.println("Responded: " + data);
            out.writeUTF(data);
        } catch(IOException e) {e.printStackTrace();}

        // finally get executed no matter what, even if exception is thrown
        finally {
            try {
                clientSocket.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * Get the data that is being send to the TCP IP Client.
     * @return string of data
     */
    public static String getData() {
        return data;
    }

    /**
     * Set the data to send to Client.
     * @param data string data
     */
    public static void setData(String data) {
        Connection.data = data;
    }
}
