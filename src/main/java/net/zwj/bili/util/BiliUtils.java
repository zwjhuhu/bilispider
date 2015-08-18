package net.zwj.bili.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class BiliUtils {

	private static final SimpleDateFormat sdf1 = new SimpleDateFormat(
			"MM/dd HH:mm");

	private static final SimpleDateFormat sdf11 = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");

	private static final SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	private BiliUtils() {

	}

	public static Date translateDate(String str) {
		Date date = null;
		try {
			date = sdf1.parse(str);
			date = sdf11.parse(Calendar.getInstance().get(Calendar.YEAR) + "/"
					+ str);

		} catch (Exception e) {
			date = null;
		}

		if (date == null) {

			try {
				date = sdf2.parse(str);
			} catch (Exception e) {
				date = null;
			}
		}
		return date;
	}

	public static Date translateDate(String str, String pattern) {
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(str);
		} catch (ParseException e) {
			// ignore
			date = null;
		}
		return date;
	}

	public static Integer parseInteger(String str) {
		Integer ret = null;
		try {
			ret = Integer.parseInt(str);
		} catch (RuntimeException e) {
			// ignore
			ret = null;
		}
		return ret;
	}
}
