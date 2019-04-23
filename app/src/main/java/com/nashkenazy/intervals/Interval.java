package com.nashkenazy.intervals;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Interval {
	public static final List<String> INTERVALS = ImmutableList.of("Perfect Unison", "Minor Second", "Major Second", "Minor Third", "Major Third", "Perfect Fourth", "Diminished Fifth/Augmented Fourth", "Perfect Fifth", "Minor Sixth", "Major Sixth", "Minor Seventh", "Major Seventh", "Perfect Octave");


	private int semitones;
	private String name;

	public Interval(int semitones) {
		this.semitones = semitones;
		this.name = INTERVALS.get(semitones);
	}

	public int getSemitones() {
		return semitones;
	}

	public String getName() {
		return name;
	}

}
