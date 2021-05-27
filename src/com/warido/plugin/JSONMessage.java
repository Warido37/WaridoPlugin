package com.warido.plugin;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;

public class JSONMessage {
	public enum HoverAction {
		TEXT {
			public String toString() {
				return "show_text";
			}
		}
	}
	
	public enum ClickAction {
		COMMAND {
			public String toString() {
				return "run_command";
			}
		},
		SUGGEST {
			public String toString() {
				return "suggest_command";
			}
		};
	}
	
	private String message = "";
	private int index = -1;
	private String currentText = "";
	private HashMap<String, HashMap<String, String>> items = new HashMap<String, HashMap<String, String>>(); 
	private HashMap<String, String> options = new HashMap<String, String>();
	
	JSONMessage () {
		
	}
	
	public void addText(String text) {
		if(index != -1) {
			this.step();
		}
		this.currentText = text;
		this.index++;
	}
	
	public void addText(String text, ChatColor color) {
		if(index != -1) {
			this.step();
		}
		this.currentText = text;
		this.options.put("color", color.name().toLowerCase());
		this.index++;
	}
	
	private void step() {
		this.items.put(currentText, options);
		this.options = new HashMap<String, String>();
	}
	
	public String toString() {
		this.step();
		message = "[";
		Set<String> keys = items.keySet();
		for(String key : keys) {
			String json = "";
			if(message != "[") {
				json = ",";
			}
			json += "{\"text\":\"" + key + "\"";
			HashMap<String, String> value = items.get(key);
			Set<String> optionKeys = value.keySet();
			for(String option : optionKeys) {
				if(option == "color") {
					json += ",\""+option+"\":\""+value.get(option)+"\"";
				}else {
					json += ",\""+option+"\":"+value.get(option);
				}
			}
			json += "}";
			message += json;
		}
		message += "]";
		return message;
	}

	public void addHoverEvent(HoverAction action, String value) {
		options.put("hoverEvent","{\"action\":\""+action.toString()+"\",\"value\":[{\"text\":\""+value+"\"}]}");
	}
	
	public void addClickEvent(ClickAction action, String value) {
		options.put("clickEvent","{\"action\":\""+action.toString()+"\",\"value\":\""+value+"\"}");
	}
}
