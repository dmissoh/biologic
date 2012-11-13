package com.dmissoh.biologic.models;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;

import com.dmissoh.biologic.utils.ModelUtils;
import com.dmissoh.biologic.viewers.IEventListListener;

public class Sequence implements ISequence {

	private List<Family> families;
	private List<Entry> logEntries;

	private ListenerList changeListeners = new ListenerList();

	private String description;
	private long startTime;
	private long endTime;
	private boolean isLive;

	public Sequence(boolean isLive) {
		this.logEntries = new ArrayList<Entry>();
		this.families = ModelUtils.getFamiliesFromPreferences();
		this.isLive = isLive;
	}

	public List<Entry> getLogEntries() {
		return logEntries;
	}

	public void addLogEntry(Entry eventEntry) {
		logEntries.add(eventEntry);
		for (Object listenerObj : changeListeners.getListeners()) {
			((IEventListListener) listenerObj).addTask(eventEntry);
		}
	}

	public void removeLogEntry(Entry eventEntry) {
		logEntries.remove(eventEntry);
		for (Object listenerObj : changeListeners.getListeners()) {
			((IEventListListener) listenerObj).removeTask(eventEntry);
		}
	}

	public void logEntryChanged(Entry eventEntry) {
		for (Object listenerObj : changeListeners.getListeners()) {
			((IEventListListener) listenerObj).updateTask(eventEntry);
		}
	}

	public void removeChangeListener(IEventListListener viewer) {
		changeListeners.remove(viewer);
	}

	public void addChangeListener(IEventListListener listener) {
		changeListeners.add(listener);
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final long getStartTime() {
		return startTime;
	}

	public final void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public final long getEndTime() {
		return endTime;
	}

	public final void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void setLogEntries(List<Entry> logEntries) {
		this.logEntries = logEntries;
	}

	public final List<Family> getFamilies() {
		return families;
	}

	public final void setFamilies(List<Family> families) {
		this.families = families;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

}
