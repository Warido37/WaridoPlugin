package com.warido.plugin;

public class Songs {
	final private float[] PITCH = { 0.5f, 0.53f, 0.56f, 0.6f, 0.63f, 0.67f, 0.7f, 0.76f, 0.8f, 0.84f, 0.9f, 0.94f, 1f,
			1.06f, 1.12f, 1.18f, 1.26f, 1.34f, 1.42f, 1.5f, 1.6f, 1.68f, 1.78f, 1.88f, 2.0f };
	final private String[] NOTE = { "F#0", "G0", "G#0", "A0", "A#0", "B0", "C1", "C#1", "D1", "D#1", "E1", "F1", "F#1",
			"G1", "G#1", "A1", "A#1", "B1", "C2", "C#2", "D2", "D#2", "E2", "F2", "F#2" };

	private String[] SONG_NAMES = { "Ode to joy", "Little star", "Among us", "why u acting sus",
			"Megalovonia", "Fur Elise", "Canon" };
	private String[] SONGS = {
			/*Ode to joy*/
			"E1 E1 F1 G1 G1 F1 E1 D1 C1 C1 D1 E1 E1 D1 D1 " + "E1 E1 F1 G1 G1 F1 E1 D1 C1 C1 D1 E1 D1 C1 C1 "
					+ "D1 D1 E1 C1 D1 E1 F1 E1 C1 D1 E1 F1 E1 D1 C1 D1 G0 "
					+ "E1 E1 E1 F1 G1 G1 F1 E1 D1 C1 C1 D1 E1 D1 C1 C1",
			/*Little star*/
			"C1 C1 G1 G1 A1 A1 G1 F1 F1 E1 E1 D1 D1 C1 G1 G1 F1 F1 E1 E1 D1 G1 G1 F1 F1 E1 E1 D1 C1 C1 G1 G1 A1 A1 G1 F1 F1 E1 E1 D1 D1 C1",
			/*Among us*/
			"C2 D#2 F2 F#2 F2 D#2 C2 A#1 D2 C2 A#0 C1 C2 D#2 F2 F#2 F2 D#2 F#2 F#2 F2 D#2 F#2 F2 D#2 C2",
			/*Why you acting sus*/
			"C#2 C#2 B1 C#2 C#2 C#2 C#2 C#1 E1 E1 E1 G#1 D#1 D#1 D#1 D#1 D#1 B0 C#1 D#1 E1 C#1 C#1 C#1 C#1 C#1 C#1 D#1 D#1 B0 D#1 D#1 D#1 D#1 E1 F#1",
			/*Megalovonia*/
			"D1 D1 D2 A1 G#1 G1 F1 D1 F1 G1 " + "C1 C1 D2 A1 G#1 G1 F1 D1 F1 G1 " + "B0 B0 D2 A1 G#1 G1 F1 D1 F1 G1 "
					+ "A#0 A#0 D2 A1 G#1 G1 F1 D1 F1 G1",
			/*Fur Elise*/
			"E2 D#2 E2 D#2 E2 B1 D2 C2 A1 C1 E1 A1 B1 E1 G#1 B1 C2 E1 "
					+ "E2 D#2 E2 D#2 E2 B1 D2 C2 A1 C1 E1 A1 B1 E1 C2 B1 A1 " + "",
			/*Canon*/
			"F#2 D#2 E2 F#2 D#2 E2 F#2 B1 B1 B1 C#2 D#2 E2 D#2 B1 C#2 D#2 F#1 F#1 F#1 G#1 F#1 E1 F#1 " 
	};
	private int LAST_RANDOM_ID = -1;

	public String getSong(int id) {
		if (id < 0 | id >= SONG_NAMES.length) {
			return "";
		}
		return SONGS[id];
	}

	public String getSong() {
		int id = (int) Math.floor(Math.random() * (double) SONGS.length);
		return SONGS[id];
	}

	public int lastSongID() {
		return LAST_RANDOM_ID;
	}

	public String getSongName(int id) {
		return SONG_NAMES[id];
	}

	public float noteToPitch(String note) {
		int i = 0;
		for (String N : NOTE) {
			if (N.contentEquals(note)) {
				return PITCH[i];
			}
			i++;
		}
		return 0.0f;
	}

	public int rotate(int id) {
		int newid = id + 1;
		if (newid >= SONGS.length) {
			newid = 0;
		}
		return newid;
	}

	public int length() {
		return SONGS.length;
	}
}
