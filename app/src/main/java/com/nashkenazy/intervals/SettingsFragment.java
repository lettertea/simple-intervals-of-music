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


		Set includedIntervalTypesForRange = includedIntervalTypes.getValues();
		if (key.equals(SettingsActivity.KEY_PREF_INCLUDED_INTERVAL_TYPES)) {
			includedIntervalTypesForRange = (Set) newValue;  // Downcasting to Set will always be safe if the preference is a MultiSelectListPreference
		}
		for (Object semitonesString : includedIntervalTypesForRange) {
			int semitones = Integer.parseInt(semitonesString.toString());
			if (intervalRange < semitones) {
				intervalRange = semitones;
			}
		}


		CharSequence[] keyboardRangeEntries = new String[7]; // There will always be 7 entries in keyboard range settings
		CharSequence[] noteEntries = noteLetter.getEntries().clone();
		Arrays.sort(noteEntries);


		int lowerNoteValue = 0;
		int upperNoteValue = 0;
		int lowerNoteOctaveOffset = 0;
		int upperNoteOctaveOffset = 0;

		System.out.println(key);

		String currentPosition = position.getValue();
		if (key.equals(SettingsActivity.KEY_PREF_POSITION)) { currentPosition = newValue.toString(); }


		String currentNoteLetter = noteLetter.getValue();
		if (key.equals(SettingsActivity.KEY_PREF_NOTE)) { currentNoteLetter = newValue.toString(); }

		if (currentPosition.equals("Upper")) {
			upperNoteValue = Integer.parseInt(currentNoteLetter);
			lowerNoteValue = upperNoteValue - intervalRange;
		} else if (currentPosition.equals("Lower")) {
			lowerNoteValue = Integer.parseInt(currentNoteLetter);
			upperNoteValue = lowerNoteValue + intervalRange;
		} else {
			upperNoteValue = 12 + intervalRange;
			lowerNoteValue = 1 - intervalRange;
		}

		if (upperNoteValue == -1) {
			upperNoteValue = 12;
			lowerNoteValue = 1 - intervalRange;
		} else if (lowerNoteValue == -1) {
			lowerNoteValue = 1;
			upperNoteValue = 12 + intervalRange;
		}
		while (lowerNoteValue < 0) {
			lowerNoteValue += 12;
			lowerNoteOctaveOffset -= 1;
		}
		while (upperNoteValue > 12) {
			upperNoteValue -= 12;
			upperNoteOctaveOffset += 1;
		}


		CharSequence lowerNoteLetter = noteEntries[lowerNoteValue - 1];
		CharSequence upperNoteLetter = noteEntries[upperNoteValue - 1];

		keyboardRangeEntries[0] = "Random";
		// Start from 1 to avoid overwriting the Random entry
		for (int i = 1; i < keyboardRangeEntries.length; i++) {
			keyboardRangeEntries[i] = lowerNoteLetter + String.valueOf(lowerNoteOctaveOffset + i) + " - " + upperNoteLetter + String.valueOf(upperNoteOctaveOffset + i);
		}

		keyboardRange.setEntries(keyboardRangeEntries);

		//  Must reset the summary because %s in preferences.xml does not update to the updated entry
		keyboardRange.setSummary(keyboardRangeEntries[Integer.parseInt(keyboardRange.getValue())]);

		System.out.println("Rchead!");
		return true;
	}

	private boolean setKeyboardRange() {

		int intervalRange = 0;

		for (Object semitonesString : includedIntervalTypes.getValues()) {
			int semitones = Integer.parseInt(semitonesString.toString());
			if (intervalRange < semitones) {
				intervalRange = semitones;
			}
		}

		CharSequence[] keyboardRangeEntries = new String[7]; // There will always be 7 entries in keyboard range settings
		CharSequence[] noteEntries = noteLetter.getEntries().clone();
		Arrays.sort(noteEntries);

		int lowerNoteValue = 0;
		int upperNoteValue = 0;
		int lowerNoteOctaveOffset = 0;
		int upperNoteOctaveOffset = 0;


		if (position.getValue().equals("Upper")) {
			upperNoteValue = Integer.parseInt(noteLetter.getValue());
			lowerNoteValue = upperNoteValue - intervalRange;
		} else if (position.getValue().equals("Lower")) {
			lowerNoteValue = Integer.parseInt(noteLetter.getValue());
			upperNoteValue = lowerNoteValue + intervalRange;
		} else {
			upperNoteValue = 12 + intervalRange;
			lowerNoteValue = 1 - intervalRange;
		}


		if (upperNoteValue == -1) {
			upperNoteValue = 12;
			lowerNoteValue = 1 - intervalRange;
		}
		if (lowerNoteValue == -1) {
			lowerNoteValue = 1;
			upperNoteValue = 12 + intervalRange;
		}

		while (lowerNoteValue < 0) {
			lowerNoteValue += 12;
			lowerNoteOctaveOffset -= 1;
		}
		while (upperNoteValue > 12) {
			upperNoteValue -= 12;
			upperNoteOctaveOffset += 1;
		}


		CharSequence lowerNoteLetter = noteEntries[lowerNoteValue - 1];
		CharSequence upperNoteLetter = noteEntries[upperNoteValue - 1];

		keyboardRangeEntries[0] = "Random";
		// Start from 1 to avoid overwriting the Random entry
		for (int i = 1; i < keyboardRangeEntries.length; i++) {
			keyboardRangeEntries[i] = lowerNoteLetter + String.valueOf(lowerNoteOctaveOffset + i) + " - " + upperNoteLetter + String.valueOf(upperNoteOctaveOffset + i);
		}

		keyboardRange.setEntries(keyboardRangeEntries);

		System.out.println("Rchead!");
		return true;
	}


}