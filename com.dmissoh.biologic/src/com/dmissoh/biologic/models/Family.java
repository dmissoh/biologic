package com.dmissoh.biologic.models;

import java.util.Stack;

public class Family {

	private String name;

	private char endKey;
	private char startKey;
	boolean isPunctual;

	private Stack<Entry> logEntries;

	public Family(String name) {
		this.name = name;
		logEntries = new Stack<Entry>();
	}

	public void push(Entry entry) {
		getLogEntries().push(entry);
	}

	public String getName() {
		return name;

	}

	public void setName(String name) {
		this.name = name;
	}

	public char getEndKey() {
		return endKey;
	}

	public void setEndKey(char endKey) {
		this.endKey = endKey;
	}

	public char getStartKey() {
		return startKey;
	}

	public void setStartKey(char startKey) {
		this.startKey = startKey;
	}

	public Stack<Entry> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(Stack<Entry> logEntries) {
		this.logEntries = logEntries;
	}

	public final boolean isPunctual() {
		return isPunctual;
	}

	public final void setPunctual(boolean isPunctual) {
		this.isPunctual = isPunctual;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Family)) {
			return false;
		}
		return ((Family) obj).getName().equals(getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

}
