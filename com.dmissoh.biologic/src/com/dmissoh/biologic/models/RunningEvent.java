package com.dmissoh.biologic.models;

public class RunningEvent implements Comparable<RunningEvent> {

	private Entry entry;
	private char startKey;
	private char endKey;

	public RunningEvent(Entry entry, char startKey, char endKey) {
		this.entry = entry;
		this.startKey = startKey;
		this.endKey = endKey;
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

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public int compareTo(RunningEvent runningEvent) {
		if(runningEvent == null){
			return 1;
		}else{
			if(this.getEntry().getTimeStamp() > runningEvent.getEntry().getTimeStamp()){
				return -1;
			}else if(this.getEntry().getTimeStamp() < runningEvent.getEntry().getTimeStamp()){
				return 1;
			}
		}
		return 0;
	}
}
