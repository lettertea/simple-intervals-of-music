package com.nashkenazy.intervals;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {
	public static final String KEY_PREF_LOWER_NOTE = "lower_note";
	public static final String KEY_PREF_OCTAVE = "octave";
	public static final String KEY_PREF_INCLUDED_INTERVALS = "included_intervals";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
}
