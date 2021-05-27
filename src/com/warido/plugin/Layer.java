package com.warido.plugin;

import java.util.ArrayList;

public class Layer {
	private String name;
	private Instrument instrument = Instrument.PIANO;
	private ArrayList<String> notes = new ArrayList<String>();
	
	private Boolean repeated = false;
	private int start = 0;
	
	Layer(String name) {
		this.name = name;
	}
	
	public void addNotes(String notes) {
		for(String note : notes.split("\\s")) {
			this.notes.add(note);
		}
	}
	
	public String getNote(int index) {
		return notes.get(index);
	}
	
	public int length() {
		return notes.size();
	}
	
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	
	public Instrument getInstrument() {
		return this.instrument;
	}
	
	public String getInstrumentPrefix() {
		return this.instrument.toString();
	}

	public String getName() {
		return name;
	}

	public void setInstrument(String name) {
		this.instrument = Instrument.getInstrument(name);
	}

	public Boolean getRepeated() {
		return repeated;
	}

	public void setRepeated(Boolean repeated) {
		this.repeated = repeated;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	
}
