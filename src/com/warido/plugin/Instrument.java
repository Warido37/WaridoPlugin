package com.warido.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

public enum Instrument {
	PIANO(Sound.NOTE_PIANO, "", 'a'),
	BASS(Sound.NOTE_BASS, "b", '9'),
	BASS_DRUM(Sound.NOTE_BASS_DRUM, "bd", '5'),
	BASS_GUITAR(Sound.NOTE_BASS_GUITAR,"bg", '9'),
	STICKS(Sound.NOTE_STICKS,"s", 'e'),
	SNARE(Sound.NOTE_SNARE_DRUM,"sd", '6'),
	JINGLE(Sound.NOTE_PLING ,"j", '3'),
	STONE(Sound.DIG_STONE, "st", '7');
	
	private String id = "";
	private Sound sound = Sound.NOTE_PIANO;
	private ChatColor color = ChatColor.GREEN;
	
	Instrument(Sound sound, String id, char color) {
		this.sound = sound;
		this.id = id;
		this.color = ChatColor.getByChar(color);
	}

	public Sound getSound() {
		return this.sound;
	}
	
	public String toString() {
		return this.id;
	}
	
	public static Instrument getInstrument(String name) {
		for(Instrument instrument : Instrument.values()) {
			if(name.equalsIgnoreCase(instrument.toString())) {
				return instrument;
			}
		}
		return Instrument.PIANO;
	}

	ChatColor getColor() {
		return this.color;
	}
}
