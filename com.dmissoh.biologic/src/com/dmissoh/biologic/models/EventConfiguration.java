package com.dmissoh.biologic.models;

public class EventConfiguration {

	private char startKey;
	private char endKey;
	private String name;
	private boolean isPunctual;

	public EventConfiguration(String name, char startKey, char endKey) {
		this.name = name;
		this.startKey = startKey;
		this.endKey = endKey;
		this.isPunctual = false;
	}

	public EventConfiguration(String name, char startKey) {
		this.name = name;
		this.startKey = startKey;
		this.endKey = '-';
		this.isPunctual = true;
	}

	public char getStartKey() {
		return startKey;
	}

	public void setStartKey(char startKey) {
		this.startKey = startKey;
	}

	public char getEndKey() {
		return endKey;
	}

	public void setEndKey(char endKey) {
		this.endKey = endKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public final boolean isPunctual() {
		return isPunctual;
	}

	public final void setPunctual(boolean isPunctual) {
		this.isPunctual = isPunctual;
	}
}
