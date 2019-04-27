package com.nashkenazy.intervals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.os.Handler;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements OnIntervalClick {

	public static final List<String> NOTES_FILE_NAMES = ImmutableList.of("a_0_1", "as_0_2", "b_0_3", "c_1_4", "cs_1_5", "d_1_6", "ds_1_7", "e_1_8", "f_1_9", "fs_1_10", "g_1_11", "gs_1_12", "a_1_13", "as_1_14", "b_1_15", "c_2_16", "cs_2_17", "d_2_18", "ds_2_19", "e_2_20", "f_2_21", "fs_2_22", "g_2_23", "gs_2_24", "a_2_25", "as_2_26", "b_2_27", "c_3_28", "cs_3_29", "d_3_30", "ds_3_31", "e_3_32", "f_3_33", "fs_3_34", "g_3_35", "gs_3_36", "a_3_37", "as_3_38", "b_3_39", "c_4_40", "cs_4_41", "d_4_42", "ds_4_43", "e_4_44", "f_4_45", "fs_4_46", "g_4_47", "gs_4_48", "a_4_49", "as_4_50", "b_4_51", "c_5_52", "cs_5_53", "d_5_54", "ds_5_55", "e_5_56", "f_5_57", "fs_5_58", "g_5_59", "gs_5_60", "a_5_61", "as_5_62", "b_5_63", "c_6_64", "cs_6_65", "d_6_66", "ds_6_67", "e_6_68", "f_6_69", "fs_6_70", "g_6_71", "gs_6_72", "a_6_73", "as_6_74", "b_6_75", "c_7_76", "cs_7_77", "d_7_78", "ds_7_79", "e_7_80", "f_7_81", "fs_7_82", "g_7_83", "gs_7_84", "a_7_85", "as_7_86", "b_7_87", "c_8_88");
	final private Handler handler = new Handler();
	final private int INTERVAL_DELAY = 600;
	final Random RAND = new Random();
	private int intervalDistance;
	private int lowerNote;
	private int upperNote;
	private SharedPreferences sharedPref;
	private MediaPlayer mediaPlayer;

	private List<Interval> includedIntervals;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		// Set the answer according to the settings
		includedIntervals = new ArrayList<>();
		for (String interval : sharedPref.getStringSet(SettingsActivity.KEY_PREF_INCLUDED_INTERVALS, null)) {
			includedIntervals.add(new Interval(Integer.parseInt(interval)));
		}
		Collections.sort(includedIntervals, (lhs, rhs) -> lhs.getSemitones() - rhs.getSemitones()); // Makes sure user sees options in order

		// Set answer options
		RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		recyclerView.setAdapter(new MyAdapter(includedIntervals, this));

		setNewRound();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Adapter listener implementation must be done here instead of setNewRound
	@Override
	public void onClick(int semitone) {
		stopAllAudio();
		if (semitone == intervalDistance) {
			Button nextIntervalBtn = findViewById(R.id.button_next_interval);
			nextIntervalBtn.setEnabled(true);
			ViewCompat.setBackgroundTintList(nextIntervalBtn, ColorStateList.valueOf(getResources().getColor(R.color.colorNextInterval)));

			playNote(88);
		} else {
			playNote(1);
		}
	}

	//  Does not setup elements in RecyclerView as it must be implemented in the class
	private void setNewRound() {

		Button repeatIntervalBtn = findViewById(R.id.button_repeat);
		Button nextIntervalBtn = findViewById(R.id.button_next_interval);


		nextIntervalBtn.setOnClickListener(v -> {

			if (!repeatIntervalBtn.isEnabled()) {
				repeatIntervalBtn.setEnabled(true);
			}

			nextIntervalBtn.setEnabled(false);
			ViewCompat.setBackgroundTintList(nextIntervalBtn, ColorStateList.valueOf(Color.LTGRAY));

			int lowerNoteSetting = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_LOWER_NOTE, "4"));
			if (lowerNoteSetting == -1) {
				lowerNoteSetting = RAND.nextInt(12) + 1;
			}

			int octave = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_OCTAVE, "4"));

			if (octave == -1) {
				octave = RAND.nextInt(6) + 1;
			}

			// Set the answer according to the settings
			lowerNote = 12 * (octave - 1) + lowerNoteSetting;
			intervalDistance = includedIntervals.get(RAND.nextInt(includedIntervals.size())).getSemitones();
			upperNote = lowerNote + intervalDistance;

			playInterval(lowerNote, upperNote);
		});

		repeatIntervalBtn.setOnClickListener(v1 -> playInterval(lowerNote, upperNote));
	}

	private void stopAllAudio() {
		if (mediaPlayer != null) { mediaPlayer.release(); }
		handler.removeCallbacksAndMessages(null);
	}

	private void playNote(int noteNumber) {
		mediaPlayer = MediaPlayer.create(this, getResources().getIdentifier(NOTES_FILE_NAMES.get(noteNumber - 1), "raw", getPackageName()));
		mediaPlayer.setOnCompletionListener(MediaPlayer::release);
		mediaPlayer.start();
	}

	private void playNoteAfterDelay(final int noteNumber, int delay) {
		handler.postDelayed(() -> playNote(noteNumber), delay);
	}

	private void playInterval(int lowerNote, int upperNote) {
		stopAllAudio();
		playNote(lowerNote);
		playNoteAfterDelay(upperNote, INTERVAL_DELAY);

		playNoteAfterDelay(upperNote, INTERVAL_DELAY * 3);
		playNoteAfterDelay(lowerNote, INTERVAL_DELAY * 4);

		playNoteAfterDelay(lowerNote, INTERVAL_DELAY * 6);
		playNoteAfterDelay(upperNote, INTERVAL_DELAY * 6);
	}

}
