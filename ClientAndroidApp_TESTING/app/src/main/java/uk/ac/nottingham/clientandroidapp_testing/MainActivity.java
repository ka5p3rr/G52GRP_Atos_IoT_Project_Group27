package uk.ac.nottingham.clientandroidapp_testing;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    String sendOut = "hello";
    /**
     * Server IP address
     */
    private String ipAddress = null;
    /**
     * Server port number
     */
    private final int PORT_NUMBER = 7896;

    /**
     * Main function that launches the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
    }

    /**
     * {@link Handler} which changes the UI when the client thread calls it
     */
    private Handler UIHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle b = msg.getData();
            String result = b.getParcelable("data");
            textView.setText(result);
            return true;
        }
    });


    /**
     * Start running the client
     * @param view
     */
    public void start(View view) {
        EditText editText = findViewById(R.id.editText);
        ipAddress = editText.getText().toString();

        // validate the input IP Address
        if(!isValidIP(ipAddress)) {
            makeToast("Please enter a valid IP address!");
            return;
        }

        // run it as a new thread
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    // connects to server and returns the data received
                    final String data = connect();
                    UIHandler.post(new Runnable() {
                        public void run() {
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
        }).start();
    }


    /**
     * Does the actual TCP IP Client Server connection
     * @return fetched string from server
     */
    private String connect() {
        Socket clientSocket = null;
        String data = null;

        try {
            clientSocket = new Socket(ipAddress, PORT_NUMBER);

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            // send out to server
            out.writeUTF(sendOut);

            // receive from server
            data = in.readUTF();
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
            if (clientSocket != null)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    Log.e("CLOSE", e.getMessage());
                }
        }

        return data;
    }


    /**
     * makes a toast pop up
     * @param text message text for the toast
     */
    private void makeToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        toast.show();
    }

    /**
     * Check if IP address is valid.
     * @param ip ip address
     * @return valid returns true, invalid returns false
     */
    public static boolean isValidIP(final String ip) {
        // Regex to check valid ip addresses
        final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return PATTERN.matcher(ip).matches();
    }
}
