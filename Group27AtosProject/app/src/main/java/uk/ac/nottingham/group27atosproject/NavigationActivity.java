package uk.ac.nottingham.group27atosproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MyThread thread = null;
    String runningAsUser;
    TextView textView;
    String sendOut = null;
    /**
     * Server IP address
     */
    private final String IP_ADDRESS = "100.74.97.17";
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
        setContentView(R.layout.activity_navigation);

        textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        runningAsUser = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        this.sendOut = runningAsUser;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // call a function to change the username and user email in the header of the navigation bar
        changeHeaderText(navigationView);

        // Check the overview as the checked item by default as the first screen
        navigationView.setCheckedItem(R.id.overview_menuitem);
        setTitle(R.string.overview_menuitem);

        start();
    }

    // when back button is pressed it either closes the navigation bar
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.signout_menuitem:
                thread.stopRunning();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeHeaderText(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        // get the text view fields to change the text
        TextView userName_textview = headerView.findViewById(R.id.userName_textview);
        TextView userMail_textview = headerView.findViewById(R.id.userMail_textview);

        // set the text itself
        switch (runningAsUser) {
            case "admin":
                userName_textview.setText(R.string.admin_username);
                userMail_textview.setText(R.string.admin_usermail);
                break;
            case "supervisor":
                userName_textview.setText(R.string.supervisor_username);
                userMail_textview.setText(R.string.supervisor_usermail);
                break;
            case "worker":
                userName_textview.setText(R.string.worker_username);
                userMail_textview.setText(R.string.worker_usermail);
                break;
        }
    }


    /* CLIENT TCP IP */

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
     */
    public void start() {
        // validate the input IP Address
        if(!isValidIP(IP_ADDRESS)) {
            makeToast("Please enter a valid IP address!");
            return;
        }

        // run it as a new thread
        thread = new MyThread();
        thread.start();
    }

    class MyThread extends Thread {
        private Boolean keepRunning = true;
        public void run() {
            while (keepRunning) {
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

        void stopRunning() {
            this.keepRunning = false;
        }
    }


    /**
     * Does the actual TCP IP Client Server connection
     * @return fetched string from server
     */
    private String connect() {
        Socket clientSocket = null;
        String data = null;

        try {
            clientSocket = new Socket(IP_ADDRESS, PORT_NUMBER);

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
