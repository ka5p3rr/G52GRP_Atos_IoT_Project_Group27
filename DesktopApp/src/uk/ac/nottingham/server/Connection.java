package uk.ac.nottingham.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/** Takes care of each connection to the Server as a new thread. */
public class Connection extends Thread {
  /** {@link DataInputStream} to reactive data from server. */
  private DataInputStream in;
  /** {@link DataOutputStream} to send data to server. */
  private DataOutputStream out;
  /** Client socket. */
  private Socket clientSocket;
  /** This is send back to the client. */
  private static String data = "N\\A";

  /**
   * Constructor. Initializes the data streams.
   *
   * @param aClientSocket socket to use for each connection
   */
  Connection(Socket aClientSocket) {
    try {
      clientSocket = aClientSocket;
      in = new DataInputStream(clientSocket.getInputStream());
      out = new DataOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>Executes this class thread. Reads from the input data from socket and sends back the data.
   */
  @Override
  public void run() {
    try {
      System.out.println("Received: " + in.readUTF()); // always needs to read the input stream
      System.out.println("Responded: " + data); // here do processing for what to sent to clients
      out.writeUTF(data); // send data
    } catch (IOException e) {
      e.printStackTrace();
    }

    // finally get executed no matter what, even if exception is thrown
    finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Get the data that is being send to the TCP IP Client.
   *
   * @return string of data
   */
  public static String getData() {
    return data;
  }

  /**
   * Set the data to send to Client.
   *
   * @param data string data
   */
  public static void setData(String data) {
    Connection.data = data;
  }

  /** Reset data to default N\A. */
  public static void resetData() {
    Connection.data = "N\\A";
  }
}
