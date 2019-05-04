package com.nashkenazy.intervals;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Interval {
	public static final List<String> INTERVALS = ImmutableList.of("P1", "m2", "M2", "m3", "M3", "P4", "d5", "P5", "m6", "M6", "m7", "M7", "P8");

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
