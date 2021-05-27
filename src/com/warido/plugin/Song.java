package com.warido.plugin;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Song {
	private String title;
	private String author;
	private String[] notes;
	private Layer[] layers;
	
	private boolean layered = false;
	private int speed = 4;
	
	Song(String title, String author, String notes){
		this.title = title;
		this.author = author;
		this.notes = notes.split(" ");
	}
	
	Song(String title, String author, String notes, int speed){
		this.title = title;
		this.author = author;
		this.notes = notes.split(" ");
		this.speed = speed;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public int getLength() {
		return notes.length;
	}
	
	public String getSongString() {
		return String.join(" ", this.notes);
	}
	
	public int getSongLength() {
		return this.getLength() * this.speed;
	}
	
	public String toString(){
		return this.author + " - " +  this.title;
	}

	public int getSpeed() {
		return this.speed;
	}

	public String getNote(int index) {
		if(index >= this.getLength()) {
			return "";
		}
		return this.notes[index];
	}

	public boolean isLayered() {
		return layered;
	}

	public void setLayered(boolean layered) {
		this.layered = layered;
	}

	public Layer[] getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers.toArray(new Layer[layers.size()]);
	}
	
	public void playSongToAll(Plugin plugin) {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for(Player player : players) {
			playSong(plugin, player);
		}
	}
	
	public void playSong(Plugin plugin, Player p) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		Songs root = new Songs();
		int i = 0;
		for(String n : notes) {
			final int index = i;
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					if(!n.equalsIgnoreCase("x")) {
						String[] blocks = n.split(",");
						for(String b : blocks) {
							float pitch = root.noteToPitch(b);
							Sound sound = root.noteToSound(b);
							p.playSound(p.getLocation(), sound, 1.0f, pitch);
						}
					}
				}
			}, getSpeed() * index);
			i++;
		}
	}
}