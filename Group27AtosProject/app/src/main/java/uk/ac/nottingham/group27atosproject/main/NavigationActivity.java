package uk.ac.nottingham.group27atosproject.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import uk.ac.nottingham.group27atosproject.R;
import uk.ac.nottingham.group27atosproject.helperclasses.ClientThread;
import uk.ac.nottingham.group27atosproject.helperclasses.NotificationManager;

public class NavigationActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  /** Thread running the TCP IP client */
  ClientThread clientThread = null;
  /** Changes depending on what user (supervisor or worker) was selected in main activity */
  String runningAsUser;
  /** {@link TextView} being changed with the fetched data from server */
  TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation);

    // text view to change with fetched data
    this.textView = findViewById(R.id.textView);

    // what user selected in the Main Activity
    Intent intent = getIntent();
    this.runningAsUser = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
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
   * {@inheritDoc}
   *
   * <p>
   *
   * <p>Overrides the back button functionality. Back button only closes the {@link DrawerLayout}
   * when open. Otherwise button press is ignored.
   */
  @Override
  public void onBackPressed() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   *
   * <p>When item selected execute this function. Switch case changed behaviour per {@link
   * MenuItem}.
   *
   * @param item {@link MenuItem} from the drawer menu
   * @return true when button clicked
   */
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    switch (id) {
      case R.id.signout_menuitem:
        goToMainActivity();
        break;
    }

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);

    return true;
  }

  /**
   * Stops running the {@link ClientThread} and cancels all ongoing notifications. Then launches the
   * {@link MainActivity} to go back.
   */
  public void goToMainActivity() {
    NotificationManager.cancelNotification(this);
    clientThread.stopRunning();
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  /**
   * Changes the header text in {@link NavigationView} with email and name per user.
   *
   * @param navigationView navigation view with the header
   */
  private void changeHeaderText(@NonNull NavigationView navigationView) {
    View headerView = navigationView.getHeaderView(0);
    // get the text view fields to change the text
    TextView userNameTextView = headerView.findViewById(R.id.userName_textview);
    TextView userMailTextView = headerView.findViewById(R.id.userMail_textview);

    // set the text itself
    switch (runningAsUser) {
      case "supervisor":
        userNameTextView.setText(R.string.supervisor_username);
        userMailTextView.setText(R.string.supervisor_usermail);
        break;
      case "worker":
        userNameTextView.setText(R.string.worker_username);
        userMailTextView.setText(R.string.worker_usermail);
        break;
    }
  }

  /** Start running the client as a new {@link ClientThread}. */
  private void startClient() {
    Handler UIHandler =
        new Handler(
            new Handler.Callback() {
              @Override
              public boolean handleMessage(Message msg) {
                Bundle b = msg.getData();
                String result = b.getParcelable("data");
                textView.setText(result);
                return true;
              }
            });

    // run it as a new thread
    clientThread = new ClientThread(UIHandler, this, textView, runningAsUser);
    clientThread.start();
  }
}
