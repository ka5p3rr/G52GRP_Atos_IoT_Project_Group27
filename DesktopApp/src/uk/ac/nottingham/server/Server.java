package uk.ac.nottingham.server;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * TCP/IP Server. Implemented as Singleton. Use {@link #getInstance()} to get an instance of this
 * class.
 */
public class Server extends Thread {
  /** Port number of the server. */
  private static final int SERVER_PORT_NUMBER = 7896;
  /** {@link Server} used to implement singleton. */
  private static Server server = null;

  /**
   * Returns an already created instance. If an object hasn't been created yet, it is created and
   * returned. If there are no active network connections it returns null.
   *
   * @return object instance
   */
  public static Server getInstance() {
    // no network connection
    if (getNetInfo().isEmpty()) {
      return null;
    }

    if (server == null) server = new Server();
    return server;
  }

  /** Private constructor is essential for singleton implementation */
  private Server() {
    System.out.println("server started");
  }

  /**
   * Retrieves the list of network interfaces. It prints out all connected interfaces with their
   * respective IPv4 addresses. It returns the list of all interfaces connected to a network. If
   * there are no network connections it returns an empty string.
   *
   * @return string with all connected interfaces and their ip addresses
   */
  public static String getNetInfo() {
    StringBuilder connectionInformation = new StringBuilder();
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        final NetworkInterface nic = interfaces.nextElement();

        // skip all loopback interfaces
        if (nic.isLoopback()) {
          continue;
        }

        for (InterfaceAddress interfaceAddress : nic.getInterfaceAddresses()) {
          final InetAddress inetAddress = interfaceAddress.getAddress();

          if (!(inetAddress instanceof Inet4Address)) {
            continue;
          }
          // print the information on screen
          connectionInformation.append(nic.getDisplayName()).append("\n");
          connectionInformation.append("\tName:    ").append(nic.getName()).append("\n");
          connectionInformation.append("\tAddress: ").append(inetAddress.getHostAddress());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return connectionInformation.toString();
  }

  /**
   * Main server method is a forever loop waiting for a connection on a client socket. Each
   * connection is handled on a new thread.
   */
  @Override
  public void run() {
    try {
      ServerSocket listenSocket = new ServerSocket(SERVER_PORT_NUMBER);
      System.out.println("server is running");
      System.out.println("\nConnection information: " + getNetInfo() + "\n");

      while (true) {
        Socket clientSocket = listenSocket.accept();
        System.out.println(
            "Connection from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
        // create the connection and start the thread
        new Connection(clientSocket).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Exit the server. */
  public void stopServer() {
    System.out.println("server stopped running");
    System.exit(0);
  }
}
