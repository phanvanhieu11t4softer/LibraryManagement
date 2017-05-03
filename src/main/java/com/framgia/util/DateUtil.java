package com.framgia.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String date_time_format = "yyyy-MM-dd HH:mm:ss";
	public static String date_format = "yyyy-MM-dd";

	public static String convertDatetoString(Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(date_format);
			return dateFormat.format(date);

		} catch (Exception e) {
			return "";
		}
	}

	public static String convertDateTimetoString(Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(date_time_format);
			return dateFormat.format(date);

		} catch (Exception e) {
			return "";
		}
	}

	public static Date convertStringtoDate(String strDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(date_format);
			return sdf.parse(strDate);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date convertStringtoDateTime(String strDate) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(date_time_format);
			Date convert = df.parse(strDate);
			return convert;

		} catch (Exception e) {
			return null;
		}
	}

	public static Date getDateNow() throws ParseException {
		DateFormat df = new SimpleDateFormat(date_time_format);
		Date date = new Date();
		Date convertedCurrentDate = df.parse(df.format(date));
		
		return convertedCurrentDate;
	}

	public static SimpleDateFormat getSimpleDateFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(date_format);
		return dateFormat;
	}
}
