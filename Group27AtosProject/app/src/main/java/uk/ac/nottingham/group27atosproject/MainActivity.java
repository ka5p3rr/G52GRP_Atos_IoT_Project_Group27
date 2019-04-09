package uk.ac.nottingham.group27atosproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "ac.uk.nottingham.group27atosproject.MESSAGE";

    /**
     * On Activity creation - launch the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Creates the menu inflater (three dots in the top right corner).
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    /**
     * Controls what to do when item from inflater touched.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // start a new intent here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Ignore all back button presses.
     */
    @Override
    public void onBackPressed() {}


    /**
     * Run {@link NavigationActivity} as "worker"
     * @param view
     */
    public void launchAsWorker(View view) {
        if (!isConnectionActive()) {
            makeToast("ERROR: no network connection");
            return;
        }
        this.launch("worker");
    }

    /**
     * Run {@link NavigationActivity} as "supervisor"
     * @param view
     */
    public void launchAsSupervisor(View view) {
        if (!isConnectionActive()) {
            makeToast("ERROR: no network connection");
            return;
        }
        this.launch("supervisor");
    }


    /**
     * Creates a new {@link Intent} to launch the {@link NavigationActivity} for a specific user.
     * @param userSelected species the user to launch the activity with
     */
    private void launch(String userSelected) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(EXTRA_MESSAGE, userSelected);
        startActivity(intent);
    }

    /**
     * Makes a toast pop up saying that there is not network connection.
     */
    private void makeToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean isConnectionActive() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
