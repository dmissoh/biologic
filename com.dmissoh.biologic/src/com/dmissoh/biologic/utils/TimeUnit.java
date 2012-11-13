package com.dmissoh.biologic.utils;

public enum TimeUnit {

	SECONDS("seconds"), MINUTES("minutes"), HOURS("hours");

	private String name;

	TimeUnit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
