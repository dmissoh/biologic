package com.dmissoh.biologic.listeners;

import java.util.List;

import com.dmissoh.biologic.models.RunningEvent;

public interface ILogEventListener {
	public abstract void handleRunningEventsChanges(
			List<RunningEvent> runningEntries);
}
