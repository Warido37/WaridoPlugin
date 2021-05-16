package com.warido.plugin;

import java.util.HashMap;

import org.bukkit.ChatColor;

public class Emotes {
	Emotes(){
		
	}
	
	public void prepareEmotes(HashMap<String,String> emotes) {
		String r = ChatColor.RESET + "";
		emotes.put("<3", cc("&c❤") + r);
		emotes.put("([Gg][Gg])", cc("&6$1") + r);
		emotes.put("(?i)(good game)(?i)", cc("&6$1!") + r);
		emotes.put("\\\\o/", cc("&d\\\\( ﾟ◡ﾟ )/") + r);
		emotes.put("o/", cc("&d( ﾟ◡ﾟ)/") + r);
		emotes.put("\\\\o", cc("&d\\\\(ﾟ◡ﾟ )") + r);
		emotes.put("OwO", cc("&dOwO") + r);
		emotes.put("UwU", cc("&dUwU") + r);
		emotes.put(">:\\(", cc("&c>:\\(") + r);
		emotes.put("\\):<", cc("&c\\):<") + r);
		emotes.put("37 ", cc("&2&kI&l&a&l37&2&kI ") + r);
		emotes.put(" 37", cc(" &2&kI&l&a&l37&2&kI") + r);
		emotes.put("homosexuality", cc("&cHo&6mo&ese&axu&bal&5it&dy") + r);
		emotes.put("gay", cc("&cho&6mo&ese&axu&bal") + r);
		emotes.put("lesbian", cc("&cho&6mo&ese&axu&bal") + r);
	}
	
	private static String cc(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
