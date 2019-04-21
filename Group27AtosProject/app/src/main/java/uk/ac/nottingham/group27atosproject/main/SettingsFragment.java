package uk.ac.nottingham.group27atosproject.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import java.util.regex.Pattern;

import uk.ac.nottingham.group27atosproject.R;

/** A simple {@link Fragment} subclass. */
public class SettingsFragment extends PreferenceFragmentCompat
    implements Preference.OnPreferenceChangeListener {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.settings_pref);

    final EditTextPreference editTextPreference =
        (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_ip_key));

    String ipAddress = editTextPreference.getText();

    if (ipAddress == null) editTextPreference.setSummary("Currently not set");
    else setIpPreferenceSummary(editTextPreference, ipAddress);

    editTextPreference.setOnPreferenceChangeListener(this);
  }

  /**
   * Check if IP address is valid.
   *
   * @param ip IP address
   * @return valid returns true, invalid returns false
   */
  public static boolean isValidIP(final String ip) {
    // Regex to check valid ip addresses
    final Pattern PATTERN =
        Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    return PATTERN.matcher(ip).matches();
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   *
   * <p>Called when the {@link EditTextPreference} of IP is changed.
   *
   * @param preference preference that has been modified
   * @param o changed object
   * @return true when preference changed, false when it doesn't get changed
   */
  @Override
  public boolean onPreferenceChange(Preference preference, Object o) {
    // validate the input IP Address
    if (!isValidIP(o.toString())) {
      Toast.makeText(getActivity(), "Please provide a valid IP address!", Toast.LENGTH_SHORT)
          .show();
      return false;
    }
    setIpPreferenceSummary(preference, o.toString());
    return true;
  }

  /**
   * Sets the summary of a {@link Preference}.
   *
   * @param preference the preference to set the summary for
   * @param text summary text
   */
  private void setIpPreferenceSummary(@NonNull Preference preference, String text) {
    preference.setSummary("Currently set to: " + text);
  }
}
