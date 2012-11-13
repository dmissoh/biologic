package com.dmissoh.biologic.models;

import java.util.ArrayList;
import java.util.List;

public class SequenceStoreObject implements ISequence {

	private String description;
	private long startTime;
	private long endTime;
	private boolean live;

	private List<Entry> logEntries = new ArrayList<Entry>();

	/*
	 * (non-Javadoc)
	 *
	 * @see com.dmissoh.biologic.models.ISequence#getDescription()
	 */
	public final String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.dmissoh.biologic.models.ISequence#getLogEntries()
	 */
	public final List<Entry> getLogEntries() {
		return logEntries;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.dmissoh.biologic.models.ISequence#setLogEntries(java.util.List)
	 */
	public final void setLogEntries(List<Entry> logEntries) {
		this.logEntries = logEntries;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.dmissoh.biologic.models.ISequence#addLogEntry(com.dmissoh.biologic
	 * .models.LogEntry)
	 */
	public void addLogEntry(Entry eventEntry) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.dmissoh.biologic.models.ISequence#setDescription(java.lang.String)
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.dmissoh.biologic.models.ISequence#getStartTime()
	 */
	public final long getStartTime() {
		return startTime;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.dmissoh.biologic.models.ISequence#setStartTime(long)
	 */
	public final void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.dmissoh.biologic.models.ISequence#getEndTime()
	 */
	public final long getEndTime() {
		return endTime;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.dmissoh.biologic.models.ISequence#setEndTime(long)
	 */
	public final void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

}
