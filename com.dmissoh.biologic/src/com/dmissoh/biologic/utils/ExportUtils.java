package com.dmissoh.biologic.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.program.Program;

import com.dmissoh.biologic.internal.PluginService;
import com.dmissoh.biologic.listeners.ISaveListener;
import com.dmissoh.biologic.models.Entry;
import com.dmissoh.biologic.models.Family;
import com.dmissoh.biologic.models.ISequence;
import com.dmissoh.biologic.models.Sequence;
import com.dmissoh.biologic.models.SequenceStoreObject;
import com.thoughtworks.xstream.XStream;

public class ExportUtils {

	private ListenerList saveListeners;

	private static ExportUtils instance;

	private XStream xstream;

	private ExportUtils() {
		saveListeners = new ListenerList();
		xstream = new XStream();
		xstream.alias("logentry", Entry.class);
		xstream.alias("sequence", SequenceStoreObject.class);
	}

	public static ExportUtils getInstance() {
		if (instance == null) {
			instance = new ExportUtils();
		}
		return instance;
	}

	public IPath getBundleLocation() {
		IPath bundlePath = PluginService.getInstance().getPlugin()
				.getStateLocation();
		return bundlePath;
	}

	public String getLogFolderPath() {
		return getBundleLocation() + File.separator + "logs";
	}

	public String getCSVFolderPath() {
		return getBundleLocation() + File.separator + "csv";
	}

	public void exportLogEntries(Sequence sequence) {

		boolean isLive = isLive(sequence);

		StringBuffer sb = new StringBuffer();
		addHeader(sb, sequence);

		String timeUnitName = TimeUtils.getTimeUnit().getName();

		if (isLive) {
			sb.append("Event Name;Event Type;Time Stamp;Elapsed Time" + " ("
					+ timeUnitName + ")");
		} else {
			sb.append("Event Name;Event Type;Elapsed Time" + " ("
					+ timeUnitName + ")");
		}
		sb.append("\n");

		File outputFile = null;

		List<Entry> entries = sequence.getLogEntries();

		long startTime = sequence.getStartTime();

		for (Entry entry : entries) {
			if (isLive) {
				long elapsed = entry.getTimeStamp() - startTime;
				float time = TimeUtils.convertTimeStampToPreferedUnits(elapsed);

				sb.append(entry.getName() + ";" + entry.getType().name() + ";"
						+ TimeUtils.formatToTime(entry.getTimeStamp()) + ";"
						+ time);
			} else {
				long timeStamp = entry.getTimeStamp();
				float time = TimeUtils
						.convertTimeStampToPreferedUnits(timeStamp);

				sb.append(entry.getName() + ";" + entry.getType().name() + ";"
						+ time);
			}
			sb.append("\n");
		}

		try {
			File csvFolder = new File(getCSVFolderPath());
			if (!csvFolder.exists()) {
				csvFolder.mkdir();
			}
			outputFile = new File(csvFolder.getAbsolutePath() + File.separator
					+ "csv_log_entries_" + sequence.getStartTime() + ".csv");
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			setContents(outputFile, sb.toString());
		} catch (Exception exp) {
			PluginService.getInstance().handleException(exp);
		}
		if (outputFile != null && outputFile.exists()) {
			Program.launch(outputFile.getAbsolutePath());
		}
	}

	public void exportGroupedEvents(Sequence sequence, List<Family> families) {
		StringBuffer sb = new StringBuffer();
		addHeader(sb, sequence);
		sb.append("Event Name;Start Time;End Time;Duration (s)");
		sb.append("\n");

		File outputFile = null;

		for (Family family : families) {
			List<Entry> entries = family.getLogEntries();

			// Make a copy
			Stack<Entry> copy = new Stack<Entry>();
			for (Entry entry : entries) {
				copy.push(entry);
			}

			while (!copy.empty()) {
				Entry entryOne = copy.pop();
				if (entryOne.getType() != Entry.TYPE.PUNCTUAL) {
					long t1 = entryOne.getTimeStamp();
					if (!copy.empty()) {
						Entry entryTwo = copy.pop();
						if (entryTwo.getType() != Entry.TYPE.PUNCTUAL) {
							long t2 = entryTwo.getTimeStamp();
							appendEvent(sb, family.getName(), t1, t2);
						} else {
							appendPunctualEvent(sb, family.getName(), entryTwo
									.getTimeStamp());
						}
					}
				} else {
					appendPunctualEvent(sb, family.getName(), entryOne
							.getTimeStamp());
				}
			}
		}

		try {
			File csvFolder = new File(getCSVFolderPath());
			if (!csvFolder.exists()) {
				csvFolder.mkdir();
			}
			outputFile = new File(csvFolder.getAbsolutePath() + File.separator
					+ "csv_grouped_event_" + sequence.getStartTime() + ".csv");
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			setContents(outputFile, sb.toString());
		} catch (Exception exp) {
			PluginService.getInstance().handleException(exp);
		}
		if (outputFile != null && outputFile.exists()) {
			Program.launch(outputFile.getAbsolutePath());
		}
	}

	private void appendEvent(StringBuffer sb, String name, long t1, long t2) {
		long timeDiff = (t1 - t2);
		float time = TimeUtils.convertTimeStampToPreferedUnits(timeDiff);

		sb.append(name + ";" + TimeUtils.formatToTime(t2) + ";"
				+ TimeUtils.formatToTime(t1) + ";" + String.valueOf(time));
		sb.append("\n");
	}

	private void appendPunctualEvent(StringBuffer sb, String name, long t) {
		sb.append(name + ";" + TimeUtils.formatToTime(t) + ";" + "0" + ";"
				+ "0");
		sb.append("\n");
	}

	private void addHeader(StringBuffer sb, Sequence sequence) {

		boolean isLive = isLive(sequence);

		sb.append("Sequence start time:;"
				+ ((isLive) ? TimeUtils.formatToDateTime(sequence
						.getStartTime()) : "not available in recorded mode"));
		sb.append("\n");

		sb.append("Sequence end time:;"
				+ ((isLive) ? TimeUtils.formatToDateTime(sequence.getEndTime())
						: "not available in recorded mode"));
		sb.append("\n");

		sb.append("Sequence description:;"
				+ ((sequence.getDescription() != null) ? sequence
						.getDescription() : "n.a"));

		sb.append("\n");
		sb.append("\n");
	}

	public void save(String selectedFile, ISequence sequence) {
		String xml = xstream.toXML(convertBusinessToStored(sequence));
		try {
			File outputFile = new File(selectedFile);
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			setContents(outputFile, xml);
		} catch (Exception exp) {
			PluginService.getInstance().handleException(exp);
		}

		notifySaveListeners();
	}

	public SequenceStoreObject restore(String path) {
		SequenceStoreObject configs = (SequenceStoreObject) xstream
				.fromXML(getContents(new File(path)));
		return configs;
	}

	private void setContents(File aFile, String aContents)
			throws FileNotFoundException, IOException {
		if (aFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		if (!aFile.exists()) {
			throw new FileNotFoundException("File does not exist: " + aFile);
		}
		if (!aFile.isFile()) {
			throw new IllegalArgumentException("Should not be a directory: "
					+ aFile);
		}
		if (!aFile.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: "
					+ aFile);
		}

		// use buffering
		Writer output = new BufferedWriter(new FileWriter(aFile));
		try {
			// FileWriter always assumes default encoding is OK!
			output.write(aContents);
		} finally {
			output.close();
		}
	}

	private String getContents(File aFile) {
		StringBuilder contents = new StringBuilder();

		try {
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return contents.toString();
	}

	public ISequence convertBusinessToStored(ISequence sequence) {
		ISequence storedSequence = new SequenceStoreObject();
		storedSequence.setDescription(sequence.getDescription());
		storedSequence.setLogEntries(sequence.getLogEntries());
		storedSequence.setStartTime(sequence.getStartTime());
		storedSequence.setEndTime(sequence.getEndTime());
		storedSequence.setLive(sequence.isLive());
		return storedSequence;
	}

	public Sequence convertStoredToBusiness(SequenceStoreObject storedSequence) {
		Sequence sequence = new Sequence(false);
		sequence.setDescription(storedSequence.getDescription());
		sequence.setLogEntries(storedSequence.getLogEntries());
		sequence.setStartTime(storedSequence.getStartTime());
		sequence.setEndTime(storedSequence.getEndTime());
		sequence.setLive(storedSequence.isLive());
		return sequence;
	}

	public void addSaveListener(ISaveListener listener) {
		saveListeners.add(listener);
	}

	public void removeSaveListener(ISaveListener listener) {
		saveListeners.remove(listener);
	}

	private void notifySaveListeners() {
		for (Object listenerObj : saveListeners.getListeners()) {
			if (listenerObj instanceof ISaveListener) {
				ISaveListener listener = (ISaveListener) listenerObj;
				listener.handleSave();
			}
		}
	}

	private boolean isLive(Sequence sequence) {
		return sequence.isLive();
	}
}
