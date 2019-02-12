package uk.ac.nottingham.group27atosproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GeneralNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_navigation);

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
        changeText(navigationView);
    }

    private void changeText(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        // creating a new intent to set the user and email address in the navigation bar
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        // get the text view fields to change the text
        TextView userName_textview = headerView.findViewById(R.id.userName_textview);
        TextView userMail_textview = headerView.findViewById(R.id.userMail_textview);

        // set the text itself
        if(message.equals("admin")) {
            userName_textview.setText(R.string.admin_username);
            userMail_textview.setText(R.string.admin_usermail);
        }
        if(message.equals("supervisor")) {
            userName_textview.setText(R.string.supervisor_username);
            userMail_textview.setText(R.string.supervisor_usermail);
        }
        if(message.equals("worker")) {
            userName_textview.setText(R.string.worker_username);
            userMail_textview.setText(R.string.worker_usermail);
        }
    }

    // when back button is pressed it either closes the navigation bar or goes back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.labelA_menu_item) {

        } else if (id == R.id.labelB_menu_item) {

        } else if (id == R.id.labelC_menu_item) {

        } else if (id == R.id.labelD_menu_item) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
