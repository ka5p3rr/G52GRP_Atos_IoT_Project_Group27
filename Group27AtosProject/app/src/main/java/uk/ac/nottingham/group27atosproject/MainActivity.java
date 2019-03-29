package uk.ac.nottingham.group27atosproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "ac.uk.nottingham.group27atosproject.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // when back button is pressed do nothing
    @Override
    public void onBackPressed() {}


    public void launchAsWorker(View view) {
        this.launch("worker");
    }

    public void launchAsSupervisor(View view) {
        this.launch("supervisor");
    }


    /**
     * Creates a new {@link Intent} to launch the {@link NavigationActivity} for a specific user.
     * @param user species the user to launch the activity with
     */
    private void launch(String user) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);
    }
}
