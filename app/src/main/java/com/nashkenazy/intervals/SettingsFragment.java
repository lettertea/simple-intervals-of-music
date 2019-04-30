package com.nashkenazy.intervals;


import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat {

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.preferences, rootKey);

		ListPreference noteLetter = (ListPreference) findPreference(SettingsActivity.KEY_PREF_NOTE_LETTAR);
		Preference pref = findPreference(SettingsActivity.KEY_PREF_FIXED_NOTE);

		pref.setOnPreferenceChangeListener((preference, newValue) -> {
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