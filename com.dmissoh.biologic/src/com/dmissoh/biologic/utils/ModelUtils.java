package com.dmissoh.biologic.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmissoh.biologic.models.Entry;
import com.dmissoh.biologic.models.EventConfiguration;
import com.dmissoh.biologic.models.Family;
import com.dmissoh.biologic.models.Sequence;
import com.dmissoh.biologic.preferences.BioLogicPreferences;

public class ModelUtils {

	public static void fillFamilies(List<Family> families, Sequence sequence) {
		for (Entry entry : sequence.getLogEntries()) {
			Family family = getFamilyForName(families, entry.getName());
			if (family != null) {
				family.getLogEntries().push(entry);
			}
		}
	}

	public static List<Family> getFamiliesFromPreferences() {
		List<Family> families = new ArrayList<Family>();
		List<EventConfiguration> configs = BioLogicPreferences.getInstance()
				.loadPersistedValueVariables();
		for (EventConfiguration eventconfig : configs) {
			Family family = new Family(eventconfig.getName());
			family.setStartKey(eventconfig.getStartKey());
			family.setEndKey(eventconfig.getEndKey());
			family.setPunctual(eventconfig.isPunctual());
			families.add(family);
		}
		return families;
	}

	public static List<Family> spitIntoFamilies(Sequence sequence) {
		Map<String, Family> nameToFamilyMap = new HashMap<String, Family>();
		for (Entry entry : sequence.getLogEntries()) {
			Family family = nameToFamilyMap.get(entry.getName());
			if (family == null) {
				family = new Family(entry.getName());
				nameToFamilyMap.put(entry.getName(), family);
			}
			family.getLogEntries().push(entry);
		}
		return new ArrayList<Family>(nameToFamilyMap.values());
	}

	public static Family getFamilyForName(List<Family> families, String name) {
		for (Family family : families) {
			if (family.getName().equals(name)) {
				return family;
			}
		}
		return null;
	}
}
