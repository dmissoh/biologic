package com.dmissoh.biologic.utils;

import java.text.SimpleDateFormat;

import com.dmissoh.biologic.Activator;

public class TimeUtils {

	static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.S");
	static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.S");

	public static String formatToTime(long time) {
		return timeFormat.format(time);
	}

	public static String formatToDateTime(long time) {
		return dateFormat.format(time);
	}

	public static TimeUnit getTimeUnit() {
		TimeUnit result = null;
		String timeUnit = Activator.getDefault().getTimeUnitPreference();
		if (timeUnit.equals(Activator.TIME_UNIT_SECONDS)) {
			result = TimeUnit.SECONDS;
		} else if (timeUnit.equals(Activator.TIME_UNIT_MINUTES)) {
			result = TimeUnit.MINUTES;
		} else if (timeUnit.equals(Activator.TIME_UNIT_HOURS)) {
			result = TimeUnit.HOURS;
		}
		return result;
	}

	public static float convertTimeStampToPreferedUnits(long timeStamp) {
		float time = 0;
		if (getTimeUnit() == TimeUnit.SECONDS) {
			time = convertMillisecondsToSeconds(timeStamp);
		} else if (getTimeUnit() == TimeUnit.MINUTES) {
			time = convertMillisecondsMinutes(timeStamp);
		} else if (getTimeUnit() == TimeUnit.HOURS) {
			time = convertMillisecondsToHours(timeStamp);
		}
		return time;
	}

	public static float convertMillisecondsToSeconds(long time) {
		return (time / 1000f);
	}

	public static float convertMillisecondsMinutes(long time) {
		return (time / (1000f * 60f));
	}

	public static float convertMillisecondsToHours(long time) {
		return (time / (1000f * 60f * 60f));
	}
}
