	package com.warido.plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.io.File;

import org.apache.commons.lang.NumberUtils;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.warido.plugin.Liberaries.TableGenerator.Alignment;
import com.warido.plugin.Liberaries.TableGenerator.Receiver;

@SuppressWarnings({ "unused", "deprecation" })
public class Songs {
	final private float[] PITCH = { 0.5f, 0.53f, 0.56f, 0.6f, 0.63f, 0.67f, 0.7f, 0.76f, 0.8f, 0.84f, 0.9f, 0.94f, 1f,
			1.06f, 1.12f, 1.18f, 1.26f, 1.34f, 1.42f, 1.5f, 1.6f, 1.68f, 1.78f, 1.88f, 2.0f };
	final private String[] NOTE = { "F#0", "G0", "G#0", "A0", "A#0", "B0", "C1", "C#1", "D1", "D#1", "E1", "F1", "F#1",
			"G1", "G#1", "A1", "A#1", "B1", "C2", "C#2", "D2", "D#2", "E2", "F2", "F#2" };
	private ArrayList<Song> SONGS = new ArrayList<Song>();
	private int LAST_RANDOM_ID = -1;
	private String ROOTFOLDER = "songs";
	
	Songs(){}
	Songs(String s){this.ROOTFOLDER = s;}
	
	public void loadSongs(Plugin p){
		try {
			File[] files = new File(p.getDataFolder().getAbsolutePath() + File.separator + ROOTFOLDER).listFiles();
			int defaultSpeed = new Song("","","").getSpeed();
			for (File file : files) {
				String[] data = file.getName().replace(".txt", "").split("-");
				Scanner reader = new Scanner(file); 
				String notes = "";
				Boolean layered = false;
				int layer = 0;
				ArrayList<Layer> layers = new ArrayList<Layer>();
				int speed = defaultSpeed;
				while (reader.hasNextLine()) {
					String line = reader.nextLine().replace('\n', ' ') + " ";
			        if(line.startsWith("@")) {
			        	line = line.replace("@", "");
		        		for(String bit : line.split("\\s")) {
		        			String[] property = bit.trim().split(":");
			        		if(property[0].equalsIgnoreCase("speed")) {
			        			speed = Integer.parseInt(property[1]);
			        		}else if(property[0].equalsIgnoreCase("layered")) {
			        			layered = true;
			        		}else if(property[0].equalsIgnoreCase("layer")) {
			        			if(layered) {
			        				layer = layers.size();
				        			layers.add(new Layer(property[1]));	
			        			}
			        		}else if(property[0].equalsIgnoreCase("instrument")) {
			        			Main.announce(property[1]);
			        			if(layered) {
			        				layers.get(layer).setInstrument(property[1]);
			        			}
			        		}else if(property[0].equalsIgnoreCase("repeat") | property[0].equalsIgnoreCase("repeated")) {
			        			if(layered) {
			        				layers.get(layer).setRepeated(true);
			        			}
			        		}else if(property[0].equalsIgnoreCase("start")) {
			        			if(NumberUtils.isNumber(property[1]) | layered) {
			        				int start = Integer.parseInt(property[1]);
			        				layers.get(layer).setStart(start);
			        			}
			        		}
		        		}
			        } else if (!line.startsWith("//") | line.equalsIgnoreCase("\n")) {
			        	if (layered) {
			        		String l = "";
			        		layers.get(layer).addNotes(line.replace("\\s(\\s+)", " "));
			        	}else {
			        		notes += line.replace("\\s(\\s+)", " ");
			        	}
			        }
			        
			    }
				reader.close();
				if(layered) {
					notes = mergeLayers(layers);
				}
				Song newSong = new Song(data[0], data[1], notes, speed);
				if(layered) {
					newSong.setLayered(true);
					newSong.setLayers(layers);
				}
				SONGS.add(newSong);
				
				System.out.print("[Warido] [SongsLoader] > loaded song: " + newSong.toString());
				if(layered) {
					System.out.print("                              layers:  " + layers.size());
				}
			}
		} catch(Error | FileNotFoundException e) {
			System.out.println("[Warido] [SongsLoader] > ERROR: " + e.getMessage());
		}
	}
	
	private String mergeNote(String oldNote, String newNote) {
		String output = "";
		if(oldNote.equalsIgnoreCase("x") | oldNote == "") {
			if(newNote.equalsIgnoreCase("x") | newNote == "") {
				output = "x";
			}else {
				output = newNote;
			}
		} else {
			if(newNote.equalsIgnoreCase("x") | newNote == "") {
				output = oldNote;
			}else{
				output = oldNote + "," + newNote;
			}
		}
		return output;
	}
	
	private String mergeLayers(ArrayList<Layer> layers) {
		ArrayList<String> track = new ArrayList<String>();
		ArrayList<Layer> toRepeat = new ArrayList<Layer>();
		for(Layer layer : layers) {
			
//			TableGenerator tg = new TableGenerator(Alignment.LEFT, Alignment.LEFT, Alignment.LEFT);
//			tg.addRow("&2Current","&2New","&2Result");
			if(layer.getRepeated()){
				toRepeat.add(layer);
			}else {
				int start = layer.getStart();
				for(int i = 0;i < layer.length(); i++) {
					String note = layer.getNote(i);
					if(!note.equalsIgnoreCase("x") & note != "") {
						note = layer.getInstrumentPrefix() + note;
					}
					String old = "";
					if((start + i) < track.size()) {
						old = track.get(start + i);
					}else {
						track.add("");
					}
					String mergedNote = mergeNote(old, note);
					track.set(start + i, mergedNote);
	//				tg.addRow(old, note, mergedNote);
				}
			}
			for(Layer rlayer : toRepeat) {
				int l = rlayer.length();
				int start = rlayer.getStart();
				int i = 0;
				for(int j = start; j < track.size(); j++) {
					String note = track.get(j);
					String note2 = rlayer.getNote(i % l);
					if(!note2.equalsIgnoreCase("x") & note2 != "") {
						note2 = rlayer.getInstrumentPrefix() + note2; 
					}
					String newNote = mergeNote(note, note2);
					track.set(j, newNote);
					i++;
				}
			}
//			for (String line : tg.generate(Receiver.CLIENT, true, true)) {
//				Main.announce(Main.cc(line));
//			}
		}
		String notes = "";
		for(String note : track) {
			notes += note + " ";
		}
		return notes.trim();
	}

	public Song getSong(int id) {
		if (id < 0 | id >= SONGS.size()) {
			return null;
		}
		return SONGS.get(id);
	}

	public Song getSong() {
		int id = (int) Math.floor(Math.random() * (double) SONGS.size());
		return SONGS.get(id);
	}

	public int lastSongID() {
		return LAST_RANDOM_ID;
	}

	public String getSongName(int id) {
		return SONGS.get(id).toString();
	}

	public float noteToPitch(String note) {
		int i = 0;
		note = note.replaceAll("[a-z]", "");
		for (String N : NOTE) {
			N = N.replaceAll("[a-z]", "");
			if (N.contentEquals(note)) {
				return PITCH[i];
			}
			i++;
		}
		return 0.0f;
	}

	public int rotate(int id) {
		int newid = id + 1;
		if (newid >= SONGS.size()) {
			newid = 0;
		}
		return newid;
	}

	public int length() {
		return SONGS.size();
	}

	public Sound noteToSound(String b) {
		return this.noteToInstrument(b).getSound();
	}
	public Instrument noteToInstrument(String b) {
		String id = b.replaceAll("([A-G](|#)\\d)", "").trim();
		return Instrument.getInstrument(id);
	}
	public String noteToSoundString(String b) {
		return this.noteToInstrument(b).toString();
	}
}
