package uk.ac.nottingham.group27atosproject.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.ac.nottingham.group27atosproject.R;

/** Settings activity that loads the {@link SettingsFragment}. */
public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
  }
}
