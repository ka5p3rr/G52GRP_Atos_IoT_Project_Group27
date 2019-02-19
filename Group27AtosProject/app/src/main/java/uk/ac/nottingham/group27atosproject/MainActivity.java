package uk.ac.nottingham.group27atosproject;

import android.content.Intent;
import android.os.Bundle;
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
    
    public void launchNavigationActivity(View view) {
        EditText editText = findViewById(R.id.user_text);
        String user = editText.getText().toString();
        switch (user) {
            case "admin":
                this.launch(user);
                break;
            case "supervisor":
                this.launch(user);
                break;
            case "worker":
                this.launch(user);
                break;
            default:
                makeToast();
        }
    }

    private void makeToast() {
        CharSequence text = "Please follow the on screen instructions!";
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        toast.show();
    }

    private void launch(String user) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);
    }
}
