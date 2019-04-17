package uk.ac.nottingham.group27atosproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /** Thread running the TCP IP client */
    ClientThread clientThread = null;
    /** Changes depending on what user (supervisor or worker) was selected in main activity */
    String runningAsUser;
    /** {@link TextView} being changed with the fetched data from server */
    TextView textView;
    /** Data being send to the server */
    String sendOut = null;
    /** Previous data value received from server*/
    int previousValue = 0;
    /** Server IP address (IPv4) */
    private final String IP_ADDRESS = "192.168.43.146";
    /** Server port number */
    private static final int PORT_NUMBER = 7896;


    /**
     * Main function that launches the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // text view to change with fetched data
        textView = findViewById(R.id.textView);

        // what user was selected in the Main Activity
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

        // Check the overview as the checked item by default (first screen in Navigation Activity)
        navigationView.setCheckedItem(R.id.overview_menuitem);
        setTitle(R.string.overview_menuitem);

        // starts the client
        startClient();
    }

    /**
     * Overrides the back button functionality. Back button only closes the {@link DrawerLayout} when open. Otherwise button press is ignored.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * When item selected execute this function. Switch case changed behaviour per {@link MenuItem}.
     * @param item {@link MenuItem} from the drawer menu
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.signout_menuitem:
                clientThread.stopRunning();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Changes the header text in {@link NavigationView} with email and name per user.
     * @param navigationView navigation view with the header
     */
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


    /* TCP IP CLIENT CODE BELOW */

    /**
     * {@link Handler} changes the UI when the client thread calls it.
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
     * Start running the client.
     */
    public void startClient() {
        // validate the input IP Address
        if(!isValidIP(IP_ADDRESS)) {
            Toast.makeText(getApplicationContext(), "Please enter a valid IP address!", Toast.LENGTH_SHORT).show();
            return;
        }

        // run it as a new thread
        clientThread = new ClientThread();
        clientThread.start();
    }

    /**
     * Inner class that extends {@link Thread} and runs the TCP IP client. On change updates the UI using {@link Handler}.
     */
    class ClientThread extends Thread {
        private Boolean keepRunning = true;

        /**
         * Connects to server every second and fetches the data. The data is used to update the UI.
         */
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

        /**
         * Does the actual TCP IP Client Server connection
         * @return fetched string from server
         */
        private String connect() {
            Socket clientSocket =  new Socket();
            String data = null;

            try {
                clientSocket.connect(new InetSocketAddress(IP_ADDRESS,  PORT_NUMBER), 1000);

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());

                // send out to server
                out.writeUTF(sendOut);

                // receive from server
                data = in.readUTF();

                if(data.contains("N\\A")) {
                    cancelNotification();
                    previousValue = 0;
                }

                if(data.contains("demo")) {
                    String[] values = data.split(",");

                    if(values.length >= 2) {
                        int i = Integer.parseInt(values[1]);
                        if(i > previousValue)
                            createNotification("Tank capacity " + i + "%");
                        if(i < previousValue)
                            cancelNotification();
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
         * Stop running the client.
         */
        void stopRunning() {
            this.keepRunning = false;
        }
    }

    /**
     * Check if IP address is valid.
     * @param ip IP address
     * @return valid returns true, invalid returns false
     */
    public static boolean isValidIP(final String ip) {
        // Regex to check valid ip addresses
        final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return PATTERN.matcher(ip).matches();
    }


    private final int NOTIFICATION_ID = 001; // a unique int for each notification
    private final String CHANNEL_ID = "100";

    /**
     * Creates a notification.
     * @param notificationContent
     */
    public void createNotification(String notificationContent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "General";
            String description = "Main notification channel of this app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning_notification_icon)
                .setContentTitle("WARNING!")
                .setContentText(notificationContent)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setColor(Color.RED)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Removes notification.
     */
    public void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
