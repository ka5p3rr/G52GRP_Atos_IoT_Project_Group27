package server;
import java.net.*;
import java.io.*;
import java.util.Enumeration;


/**
 * TCP/IP Server. Implemented as Singleton. Use {@link #getInstance()} to get an instance of this class.
 */
public class Server extends Thread {
	private final int serverPortNumber = 7896;
	private static Server server = null;

	/**
	 * Returns an already created instance. If an object hasn't been created yet, it is created and returned.
	 * @return object instance
	 */
	public static Server getInstance() {
		if(server == null)
			server = new Server();
		return server;
	}

	/**
	 * Retrieves the list of network interfaces. It prints out all connected interfaces with their respective IPV4 addresses.
	 * @return string with all connected interfaces and their ip addresses
	 */
	private String getNetInfo() {
		StringBuilder connectionInformation = new StringBuilder("Connection information:\n");
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while(interfaces.hasMoreElements()) {
				final NetworkInterface nic = interfaces.nextElement( );

				// skip all loopback interfaces
				if (nic.isLoopback())
					continue;

				for (InterfaceAddress addr : nic.getInterfaceAddresses()) {
					final InetAddress inet_addr = addr.getAddress( );

					if (!(inet_addr instanceof Inet4Address))
						continue;

					connectionInformation.append(nic.getDisplayName()).append("\n");
					connectionInformation.append("\tName:    ").append(nic.getName()).append("\n");
					connectionInformation.append("\tAddress: ").append(inet_addr.getHostAddress());
					connectionInformation.append("\n\n");
				}
			}
		} catch (IOException e){e.printStackTrace();}

		return connectionInformation.toString();
	}

	/**
	 * Main server method is a forever loop waiting for a connection on a client socket. Each connection is handled on a new thread.
	 */
	public void run() {
		try {
			ServerSocket listenSocket = new ServerSocket(serverPortNumber);
			System.out.println("server is running");
			System.out.println(getNetInfo());

			while (true) {
				Socket clientSocket = listenSocket.accept();
				System.out.println("Connection from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
				new Connection(clientSocket);
			}
		} catch(IOException e) {e.printStackTrace();}
	}

	/**
	 * Exit the server
	 */
	public static void stopServer() {
		System.out.println("server stopped running");
		System.exit(0);
	}
}