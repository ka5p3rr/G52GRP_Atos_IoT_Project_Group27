package uk.ac.nottingham.group27atosproject.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import uk.ac.nottingham.group27atosproject.R;

/**
 * Main app activity that shows two buttons to launch {@link NavigationActivity} and a settings
 * button that leads to {@link SettingsActivity}.
 */
public class MainActivity extends AppCompatActivity {
  public static final String EXTRA_MESSAGE = "ac.uk.nottingham.group27atosproject.MESSAGE";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // opens up setting context menu
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.settings_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // open the settings activity
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   *
   * <p>Ignores all back button presses.
   */
  @Override
  public void onBackPressed() {}

  /**
   * Run {@link NavigationActivity} as "worker".
   *
   * @param view
   */
  public void launchAsWorker(View view) {
    if (!isNetworkFunctioning()) return;
    this.launch("worker");
  }

  /**
   * Run {@link NavigationActivity} as "supervisor".
   *
   * @param view
   */
  public void launchAsSupervisor(View view) {
    if (!isNetworkFunctioning()) return;
    this.launch("supervisor");
  }

  /**
   * Creates a new {@link Intent} to launch the {@link NavigationActivity} for a specific user.
   *
   * @param userSelected species the user to launch the activity with
   */
  private void launch(String userSelected) {
    Intent intent = new Intent(this, NavigationActivity.class);
    intent.putExtra(EXTRA_MESSAGE, userSelected); // send over a mesage
    startActivity(intent);
  }

  /**
   * Checks whether network status. Calls {@link #isConnectionActive()} (to check for active network
   * connection) and {@link #isIpAddressSet()} (to check whether the server IP address was set in
   * the preferences);
   *
   * @return true if no network problem else returns false
   */
  private boolean isNetworkFunctioning() {
    if (!isConnectionActive()) {
      makeToast(getString(R.string.error_network));
      return false;
    }
    if (!isIpAddressSet()) {
      makeToast(getString(R.string.error_ip_address));
      return false;
    }
    return true;
  }

  /**
   * Checks whether the device is connected to a network. Returns if there is an active network
   * connection. Returns false when no network found.
   *
   * @return true - exits active connection, false - no connection
   */
  private boolean isConnectionActive() {
    ConnectivityManager connManager =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }

  /**
   * Checks whether the IP address has been set in the preferences. If not returns false, if yes
   * returns true.
   *
   * @return true if IP set else false
   */
  private boolean isIpAddressSet() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    String ipAddress = prefs.getString(getString(R.string.pref_ip_key), null);
    return ipAddress != null;
  }

  /** Makes a toast pop up saying that there is not network connection. */
  private void makeToast(String message) {
    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
    toast.show();
  }
}
