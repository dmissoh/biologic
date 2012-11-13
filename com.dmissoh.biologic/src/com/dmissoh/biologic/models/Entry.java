package com.dmissoh.biologic.models;

public class Entry {

	private Long timestamp;
	private String name = "?";
	private TYPE type;

	public enum TYPE {
		START, END, PUNCTUAL
	};

	public Entry(Long time) {
		setTimeStamp(time);
	}

	public Long getTimeStamp() {
		return timestamp;
	}

	public String getName() {
		return name;
	}

	public TYPE getType() {
		return this.type;
	}

	public void setTimeStamp(Long time) {
		timestamp = time;
	}

	public void setName(String string) {
		name = string;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

}
