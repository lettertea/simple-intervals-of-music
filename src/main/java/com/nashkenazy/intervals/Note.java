package com.nashkenazy.intervals;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.lang.reflect.Field;

public class Note implements Comparable<Note> {
    private String name;
    private int octave;
    private int number;

    // Expose number as a UID
    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(Note obj) {
        return this.number-((Note)obj).getNumber();

    }

    public Note(String fileName) {
        String fileNameArr[] = fileName.split("_");
        name = fileNameArr[0];
        octave = Integer.parseInt(fileNameArr[1]);
        number = Integer.parseInt(fileNameArr[2]);
    }

}
