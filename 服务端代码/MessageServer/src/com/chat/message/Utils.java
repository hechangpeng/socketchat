package com.chat.message;
/**
 * Date: 2018-09-25 16:40:49
 * Author: HechangPeng
 * Command: package shade:shade
 */

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public final class Utils {
	private Utils() {

	}

	public static boolean isTextEmpty(String value) {
		if (value == null || "".equals(value)) {
			return true;
		}
		return false;
	}

	public static String stampToDate(long millsec) {
		if (millsec == 0) {
			return "";
		}
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(millsec);
			return simpleDateFormat.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static String getCurrentTime() {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(getUtcTimeMills());
			return simpleDateFormat.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static long dateToStamp(String time) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = simpleDateFormat.parse(time);
			return date.getTime();
		} catch (Exception e) {
			return System.currentTimeMillis();
		}
	}

	public static String addZero(int num) {
		if (num < 10) {
			return "0" + num;
		}
		return num + "";
	}

	public static long getUtcTimeMills() {
		try {
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int second = c.get(Calendar.SECOND);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			String timeString = year + "-" + addZero(month) + "-" + addZero(date) + " " + addZero(hour) + ":"
					+ addZero(minute) + ":" + addZero(second);
			Date d = sdf.parse(timeString);
			return d.getTime();
		} catch (Exception e) {
			return System.currentTimeMillis();
		}
	}

	public static String randomColor() {
		Random random = new Random();
		int r = random.nextInt(200);
		int g = random.nextInt(200);
		int b = random.nextInt(200);
		Color color = new Color(r, g, b);
		String R = Integer.toHexString(color.getRed());
		R = R.length() < 2 ? ('0' + R) : R;
		String B = Integer.toHexString(color.getBlue());
		B = B.length() < 2 ? ('0' + B) : B;
		String G = Integer.toHexString(color.getGreen());
		G = G.length() < 2 ? ('0' + G) : G;
		return '#' + R + G + B;
	}
}
