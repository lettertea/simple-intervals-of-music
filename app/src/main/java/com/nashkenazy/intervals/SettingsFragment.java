package com.nashkenazy.intervals;


import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat {

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.preferences, rootKey);

		ListPreference noteLetter = (ListPreference) findPreference(SettingsActivity.KEY_PREF_NOTE);
		ListPreference position = (ListPreference) findPreference(SettingsActivity.KEY_PREF_POSITION);

		// Check to disable note letter on initialization
		if (position.getValue().equals("None")) {
			noteLetter.setEnabled(false);
			noteLetter.setValue("-1");  // -1 is the value for "Random"
		}

		// Check to disable note letter on change
		position.setOnPreferenceChangeListener((preference, newValue) -> {
			if (newValue.toString().equals("None")) {
				noteLetter.setEnabled(false);
				noteLetter.setValue("-1");  // -1 is the value for "Random"
			} else if (!newValue.toString().equals("None") && !noteLetter.isEnabled()) {
				noteLetter.setEnabled(true);
			}

			return true;
		});
	}
}