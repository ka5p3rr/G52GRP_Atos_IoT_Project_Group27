package uk.ac.nottingham.clientandroidapp_testing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Client client;
    private String ip = null;
    private final int port = 7896;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
     * Start running the thread with the TCP IP Client.
     * It also checks if the IP address entered is valid
     * @param view
     */
    public void start(View view) {
        EditText editText = findViewById(R.id.editText);
        ip = editText.getText().toString();

        if(!isValidIP(ip)) {
            makeToast("Please enter a valid IP address!");
            return;
        }

        client = new Client(this, ip, port);
        client.execute();
    }

    // stop running the Client
    public void stop(View view) {
        if(client != null)
            client.stopRunning();
    }

    /**
     * Regex to check valid ip addresses
     */
    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    /**
     * Check if IP address is valid.
     * @param ip ip address
     * @return valid returns true, invalid returns false
     */
    public static boolean isValidIP(final String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
