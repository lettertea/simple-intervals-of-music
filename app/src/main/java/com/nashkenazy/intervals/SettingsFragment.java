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

		disablePositionWhenNoFixedNote();
		position.setOnPreferenceChangeListener((preference, newValue) -> {
			setKeyboardRange(preference, newValue);
			disableFixedNoteWhenRandomPosition(newValue);
			return true;
		});


	}

	private void disableFixedNoteWhenRandomPosition(Object newValue) {
		if (newValue.toString().equals("Random")) {
			noteLetter.setEnabled(false);
			noteLetter.setValue("-1");
		} else if (!newValue.toString().equals("Random") && !noteLetter.isEnabled()) {
			noteLetter.setEnabled(true);
		}
	}

	private void disablePositionWhenNoFixedNote() {
		if (position.getValue().equals("Random")) {
			noteLetter.setEnabled(false);
			noteLetter.setValue("-1");
		}
	}

	private boolean setKeyboardRange(Preference preference, Object newValue) {

		int intervalRange = 0;
		String key = preference.getKey();
		String currentPosition = position.getValue();
		String currentNoteValue = noteLetter.getValue();

		Set includedIntervalTypesForRange = includedIntervalTypes.getValues();

		if (key.equals(SettingsActivity.KEY_PREF_INCLUDED_INTERVAL_TYPES)) {
			includedIntervalTypesForRange = (Set) newValue;
		}
		if (key.equals(SettingsActivity.KEY_PREF_POSITION)) { currentPosition = newValue.toString(); }
		if (key.equals(SettingsActivity.KEY_PREF_NOTE)) { currentNoteValue = newValue.toString(); }

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

			switch (currentPosition) {
				case "Upper":
					upperNoteValue = 12 * (octaveOffset - 1) + Integer.parseInt(currentNoteValue);
					lowerNoteValue = upperNoteValue - intervalRange;
					break;
				default: // Default accommodates both lower and random positions
					lowerNoteValue = 12 * (octaveOffset - 1) + Integer.parseInt(currentNoteValue);
					upperNoteValue = lowerNoteValue + intervalRange;
			}

			String lowerNoteLetter = noteEntries[(lowerNoteValue - 1) % 12].toString();
			String upperNoteLetter = noteEntries[(upperNoteValue - 1) % 12].toString();
			// Add 8 to begin 1 octave at C and apply 0 octave to A, A#, B.
			String lowerNoteOctave = String.valueOf((lowerNoteValue + 8) / 12);
			String upperNoteOctave = String.valueOf((upperNoteValue + 8) / 12);

			keyboardRangeEntries[i] = lowerNoteLetter + lowerNoteOctave + " - " + upperNoteLetter + upperNoteOctave;
		}

		keyboardRange.setEntries(keyboardRangeEntries);

		//  Must reset the summary because %s in preferences.xml does not update to the updated entry
		keyboardRange.setSummary(keyboardRangeEntries[Integer.parseInt(keyboardRange.getValue())]);
		return true;
	}

	// Workaround to use default arguments
	private boolean setKeyboardRange() {
		return setKeyboardRange(noteLetter, noteLetter.getValue()); // The arguments used doesn't matter. Just need to be valid.
	}


}