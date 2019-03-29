package uk.ac.nottingham.group27atosproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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

        // Check the overview as the checked item by default as the first screen
        navigationView.setCheckedItem(R.id.overview_menuitem);
        changeFragment(new OverviewFragment(), R.string.overview_menuitem);
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
            case R.id.overview_menuitem:
                changeFragment(new OverviewFragment(), R.string.overview_menuitem);
                break;
            case R.id.signout_menuitem:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment fragment, int title) {
        setTitle(title);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_framelayout, fragment).commit();
    }

    private void changeText(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        // creating a new intent to set the user and email address in the navigation bar
        Intent intent = getIntent();
        String user = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        // get the text view fields to change the text
        TextView userName_textview = headerView.findViewById(R.id.userName_textview);
        TextView userMail_textview = headerView.findViewById(R.id.userMail_textview);

        // set the text itself
        switch (user) {
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
}
