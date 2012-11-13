package com.dmissoh.biologic.models;

import java.util.List;

public interface ISequence {

	public abstract String getDescription();

	public abstract List<Entry> getLogEntries();

	public abstract void setLogEntries(List<Entry> logEntries);

	public abstract void addLogEntry(Entry eventEntry);

	public abstract void setDescription(String description);

	public abstract long getStartTime();

	public abstract void setStartTime(long startTime);

	public abstract long getEndTime();

	public abstract void setEndTime(long endTime);

	public abstract boolean isLive();

	public abstract void setLive(boolean isLive);

}