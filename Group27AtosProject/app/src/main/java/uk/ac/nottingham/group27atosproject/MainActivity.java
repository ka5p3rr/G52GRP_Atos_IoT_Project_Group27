package uk.ac.nottingham.group27atosproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "ac.uk.nottingham.group27atosproject.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchActivity(View view) {
        EditText editText = findViewById(R.id.email_text);
        String user = editText.getText().toString();
        if(user.equals("admin"))
            this.launchNavigationActivity(user);
        if(user.equals("supervisor"))
            this.launchNavigationActivity(user);
        if(user.equals("worker"))
            this.launchNavigationActivity(user);
    }

    private void launchNavigationActivity(String user) {
        Intent intent = new Intent(this, GeneralNavigationActivity.class);
        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);
    }
}
