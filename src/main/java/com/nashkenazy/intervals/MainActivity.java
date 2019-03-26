package com.nashkenazy.intervals;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    public static final String[] INTERVALS = {"Perfect Unison", "Minor Second", "Major Second", "Minor Third", "Major Third", "Perfect Fourth", "Diminished Fifth/Augmented Fourth", "Perfect Fifth", "Minor Sixth", "Major Sixth", "Minor Seventh", "Major Seventh", "Perfect Octave"};
    public static final String[] NOTES_FILE_NAMES = {"a_0_1", "as_0_2", "b_0_3", "c_1_4", "cs_1_5", "d_1_6", "ds_1_7", "e_1_8", "f_1_9", "fs_1_10", "g_1_11", "gs_1_12", "a_1_13", "as_1_14", "b_1_15", "c_2_16", "cs_2_17", "d_2_18", "ds_2_19", "e_2_20", "f_2_21", "fs_2_22", "g_2_23", "gs_2_24", "a_2_25", "as_2_26", "b_2_27", "c_3_28", "cs_3_29", "d_3_30", "ds_3_31", "e_3_32", "f_3_33", "fs_3_34", "g_3_35", "gs_3_36", "a_3_37", "as_3_38", "b_3_39", "c_4_40", "cs_4_41", "d_4_42", "ds_4_43", "e_4_44", "f_4_45", "fs_4_46", "g_4_47", "gs_4_48", "a_4_49", "as_4_50", "b_4_51", "c_5_52", "cs_5_53", "d_5_54", "ds_5_55", "e_5_56", "f_5_57", "fs_5_58", "g_5_59", "gs_5_60", "a_5_61", "as_5_62", "b_5_63", "c_6_64", "cs_6_65", "d_6_66", "ds_6_67", "e_6_68", "f_6_69", "fs_6_70", "g_6_71", "gs_6_72", "a_6_73", "as_6_74", "b_6_75", "c_7_76", "cs_7_77", "d_7_78", "ds_7_79", "e_7_80", "f_7_81", "fs_7_82", "g_7_83", "gs_7_84", "a_7_85", "as_7_86", "b_7_87", "c_8_88"};
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        Spinner dropdown = findViewById(R.id.spinner_choices);
        Button soundBtn = findViewById(R.id.button_start);
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNote(40);
                playNote(47);
                playNote(53);
            }
        });

        dropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, INTERVALS));
    }

    public void playNote(int noteNumber) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, getResources().getIdentifier(NOTES_FILE_NAMES[noteNumber - 1],
                "raw", getPackageName()));
        mediaPlayer.start();
    }

}
