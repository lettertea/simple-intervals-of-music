package com.nashkenazy.intervals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {
	public static final String KEY_PREF_POSITION = "position";
	public static final String KEY_PREF_NOTE = "note";
	public static final String KEY_PREF_OCTAVE = "octave";
	public static final String KEY_PREF_INCLUDED_INTERVALS = "included_intervals";
	public static final String KEY_PREF_INTERVAL_TYPE = "interval_type";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();


	}



}
