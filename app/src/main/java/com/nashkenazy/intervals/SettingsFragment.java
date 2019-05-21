package com.nashkenazy.intervals;


import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Arrays;
import java.util.Set;


public class SettingsFragment extends PreferenceFragmentCompat {

	ListPreference noteLetter;
	ListPreference position;
	MultiSelectListPreference includedIntervalTypes;
	ListPreference keyboardRange;

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.preferences, rootKey);

		noteLetter = (ListPreference) findPreference(SettingsActivity.KEY_PREF_NOTE);
		position = (ListPreference) findPreference(SettingsActivity.KEY_PREF_POSITION);
		includedIntervalTypes = (MultiSelectListPreference) findPreference(SettingsActivity.KEY_PREF_INCLUDED_INTERVAL_TYPES);
		keyboardRange = (ListPreference) findPreference(SettingsActivity.KEY_PREF_OCTAVE);

		setKeyboardRange();
		includedIntervalTypes.setOnPreferenceChangeListener(this::setKeyboardRange);
		noteLetter.setOnPreferenceChangeListener(this::setKeyboardRange);
		position.setOnPreferenceChangeListener(this::setKeyboardRange);
		keyboardRange.setOnPreferenceChangeListener(this::setKeyboardRange);

	}


	private boolean setKeyboardRange(Preference preference, Object newValue) {

		int intervalRange = 0;
		String key = preference.getKey();

		String currentPosition = position.getValue();
		String currentNoteValue = noteLetter.getValue();
		String currentKeyboardRangeValue = keyboardRange.getValue();
		Set includedIntervalTypesForRange = includedIntervalTypes.getValues();

		if (key.equals(SettingsActivity.KEY_PREF_POSITION)) { currentPosition = newValue.toString(); }
		if (key.equals(SettingsActivity.KEY_PREF_NOTE)) { currentNoteValue = newValue.toString(); }
		if (key.equals(SettingsActivity.KEY_PREF_OCTAVE)) { currentKeyboardRangeValue = newValue.toString(); }
		if (key.equals(SettingsActivity.KEY_PREF_INCLUDED_INTERVAL_TYPES)) { includedIntervalTypesForRange = (Set) newValue; }


		for (Object semitonesString : includedIntervalTypesForRange) {
			int semitones = Integer.parseInt(semitonesString.toString());
			if (intervalRange < semitones) {
				intervalRange = semitones;
			}
		}

		CharSequence[] keyboardRangeEntries = new String[6];
		CharSequence[] noteEntries = noteLetter.getEntries().clone();
		Arrays.sort(noteEntries);

		int lowerNoteValue;
		int upperNoteValue;

		keyboardRangeEntries[0] = "Random";
		// Start from 1 to avoid overwriting the Random entry
		for (int i = 1; i < keyboardRangeEntries.length; i++) {
			int octaveOffset = i + 1;

			if (currentNoteValue.equals("-1")) {
				switch (currentPosition) {
					case "Upper":
						upperNoteValue = 12 * (octaveOffset - 1) + 12;
						lowerNoteValue = 12 * (octaveOffset - 1) + 1 - intervalRange;
						break;
					default:
						lowerNoteValue = 12 * (octaveOffset - 1) + 1;
						upperNoteValue = 12 * (octaveOffset - 1) + 12 + intervalRange;
				}
			} else {
				switch (currentPosition) {
					case "Upper":
						upperNoteValue = 12 * (octaveOffset - 1) + Integer.parseInt(currentNoteValue);
						lowerNoteValue = upperNoteValue - intervalRange;
						break;
					default:
						lowerNoteValue = 12 * (octaveOffset - 1) + Integer.parseInt(currentNoteValue);
						upperNoteValue = lowerNoteValue + intervalRange;
				}
			}

			String lowerNoteLetter = noteEntries[(lowerNoteValue - 1) % 12].toString();
			String upperNoteLetter = noteEntries[(upperNoteValue - 1) % 12].toString();
			// Add 8 to begin octave 1 at C and apply octave 0 to A, A#, B.
			String lowerNoteOctave = String.valueOf((lowerNoteValue + 8) / 12);
			String upperNoteOctave = String.valueOf((upperNoteValue + 8) / 12);

			keyboardRangeEntries[i] = lowerNoteLetter + lowerNoteOctave + " - " + upperNoteLetter + upperNoteOctave;
		}

		keyboardRange.setEntries(keyboardRangeEntries);

		//  Must reset the summary because %s in preferences.xml does not update to the updated entry
		int selectedRangeIndex = Math.abs(Integer.parseInt(currentKeyboardRangeValue)) -1; // Use abs to make -1, Random, to 0 index
		keyboardRange.setSummary(keyboardRangeEntries[selectedRangeIndex]);
		return true;
	}

	// Workaround to use default arguments
	private boolean setKeyboardRange() {
		return setKeyboardRange(noteLetter, noteLetter.getValue()); // What arguments are used don't matter. Just need to be valid.
	}

}