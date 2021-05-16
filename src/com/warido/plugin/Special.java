package com.warido.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class Special {
	public String name = "magicwand";
	public String alias = "mw";
	public List<String> lore = new ArrayList<String>();  
	public String styledName = ChatColor.BOLD + "" +  ChatColor.GOLD + "Magic Wand";
	public int itemId = 280;
	public Special(String name, String alias, String styled, int id, String...lore) {
		this.name = name;
		this.alias = alias;
		this.styledName = styled;
		this.itemId = id;
		for(int i = 0;i < lore.length;i ++) {
			this.lore.add(lore[i]);
		}
	}
	
	public String toString() {
		return name;
	}
}
