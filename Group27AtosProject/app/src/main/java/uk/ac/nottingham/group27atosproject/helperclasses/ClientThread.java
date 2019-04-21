package uk.ac.nottingham.group27atosproject.helperclasses;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import uk.ac.nottingham.group27atosproject.R;
import uk.ac.nottingham.group27atosproject.main.NavigationActivity;

/**
 * Class that extends {@link Thread} and runs the TCP IP client. On change updates the UI using
 * {@link Handler}.
 */
public class ClientThread extends Thread {
  private Boolean keepRunning = true;
  private Handler UIHandler;
  /** Server IP address (IPv4) */
  private String ipAddress;
  /** Server port number */
  private static final int PORT_NUMBER = 7896;
  /** Previous data value received from server */
  private int previousValue = 0;
  /** {@link NavigationActivity} used to access it's methods */
  private NavigationActivity navigationActivity;
  /**
   * {@link TextView} UI component that is being updated from the {@link ClientThread} using a
   * {@link Handler}.
   */
  private TextView textView;

  private String messageToServer;

  /**
   * Constructor.
   *
   * @param UIHandler needs a {@link Handler} to change the UI from a worker thread
   * @param navigationActivity {@link NavigationActivity}
   * @param textView UI {@link TextView} component to update from this {@link ClientThread}
   * @param messageToServer message that is sent to the server
   */
  public ClientThread(
      Handler UIHandler,
      NavigationActivity navigationActivity,
      TextView textView,
      String messageToServer) {
    this.UIHandler = UIHandler;
    this.navigationActivity = navigationActivity;
    this.textView = textView;
    this.messageToServer = messageToServer;

    // get the server ip address set in the app preferences
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(navigationActivity);
    ipAddress = prefs.getString(navigationActivity.getString(R.string.pref_ip_key), null);
  }

  /**
   * * {@inheritDoc}
   *
   * <p>
   *
   * <p>Connects to server every second and fetches the data. The data is used to update the UI.
   */
  @Override
  public void run() {
    while (keepRunning) {
      // connects to server and returns the data received
      final String data = connect();

      UIHandler.post(
          new Runnable() {

            @Override
            public void run() {

              if (data == null) {
                navigationActivity.goToMainActivity();
                Toast toast =
                    Toast.makeText(
                        navigationActivity,
                        navigationActivity.getString(R.string.error_server),
                        Toast.LENGTH_SHORT);
                toast.show();
              }

              textView.setText(data);
            }
          });

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Log.e("INTERRUPTED", e.getMessage());
      }
    }
  }

  /**
   * Does the actual TCP IP Client Server connection. Returns {@link String} received from server.
   * Can return null when no data received.
   *
   * @return fetched string from server
   */
  private String connect() {
    Socket clientSocket = new Socket();
    String data = null;

    try {
      clientSocket.connect(new InetSocketAddress(ipAddress, PORT_NUMBER), 1000);

      DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
      DataInputStream in = new DataInputStream(clientSocket.getInputStream());

      // send out to server
      out.writeUTF(messageToServer);

      // receive from server
      data = in.readUTF();

      if (data.contains(navigationActivity.getString(R.string.default_data))) {
        NotificationManager.cancelNotification(navigationActivity);
        previousValue = 0;
      } else if (data.contains("demo")) {
        String[] values = data.split(",");

        if (values.length >= 2) {
          int i = Integer.parseInt(values[1]);
          if (i > previousValue) {
            NotificationManager.createNotification("Tank capacity " + i + "%", navigationActivity);
          }
          if (i < previousValue) {
            NotificationManager.cancelNotification(navigationActivity);
          }
          previousValue = i;

          data = "Tank capacity " + i + "%";
        }
      }

      Log.i("RECEIVED", data);

    } catch (UnknownHostException e) {
      Log.e("SOCKET", e.getMessage());
    } catch (EOFException e) {
      Log.e("EOF", e.getMessage());
    } catch (IOException e) {
      Log.e("IO", e.getMessage());
    }

    // This piece of code is executed no matter what. Even when an Exception is thrown.
    finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        Log.e("CLOSE", e.getMessage());
      }
    }

    return data;
  }

  /** Stop running the client. */
  public void stopRunning() {
    this.keepRunning = false;
  }
}
