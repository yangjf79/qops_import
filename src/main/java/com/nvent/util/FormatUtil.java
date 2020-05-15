package com.nvent.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {
	//public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//public static final DateFormat dateHourMinuteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	//public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	//public static final DateFormat yearMonthFormat = new SimpleDateFormat("yyyy-MM");
	
	public static final NumberFormat floatFormat_0 = new DecimalFormat("#");
    public static DecimalFormat FLOAT_FORMAT = new DecimalFormat();
    public static DecimalFormat FLOAT_FORMAT_0 = new DecimalFormat("0");
    public static DecimalFormat FLOAT_FORMAT_1 = new DecimalFormat("#.#");
    public static DecimalFormat FLOAT_FORMAT_2 = new DecimalFormat("0.00");

	private static ThreadLocal<DateFormat> dateHourMinuteFormat = new ThreadLocal<DateFormat>();
	private static ThreadLocal<DateFormat> hourMinuteFormat = new ThreadLocal<DateFormat>();
    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>(); 
    private static ThreadLocal<DateFormat> shortDateFormat = new ThreadLocal<DateFormat>(); 
    private static ThreadLocal<DateFormat> yearMonthFormat = new ThreadLocal<DateFormat>(); 
    private static ThreadLocal<DateFormat> MonthDayFormat = new ThreadLocal<DateFormat>(); 
    private static ThreadLocal<DateFormat> yearFormat = new ThreadLocal<DateFormat>(); 
    private static ThreadLocal<DateFormat> ymdhmsFormat = new ThreadLocal<DateFormat>(); 
    
    private static ThreadLocal<NumberFormat> floatFormat0 = new ThreadLocal<NumberFormat>(); 
    private static ThreadLocal<NumberFormat> floatFormat1 = new ThreadLocal<NumberFormat>(); 
    private static ThreadLocal<NumberFormat> floatFormat2 = new ThreadLocal<NumberFormat>(); 
    private static ThreadLocal<NumberFormat> floatFormat4 = new ThreadLocal<NumberFormat>(); 
 
	public static DateFormat getDateHourMinuteFormat() {
		DateFormat df = dateHourMinuteFormat.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			dateHourMinuteFormat.set(df);
		}
		return df;
	}
	
	public static DateFormat getHourMinuteFormat() {
		DateFormat df = hourMinuteFormat.get();
		if (df == null) {
			df = new SimpleDateFormat("HH:mm");
			hourMinuteFormat.set(df);
		}
		return df;
	}

	public static DateFormat getDateFormat() {
		DateFormat df = dateFormat.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.set(df);
		}
		return df;
	}
	public static DateFormat getMonthDayFormat() {
		DateFormat df = dateFormat.get();
		if (df == null) {
			df = new SimpleDateFormat("MM-dd");
			MonthDayFormat.set(df);
		}
		return df;
	}

	public static DateFormat getShortDateFormat() {
		DateFormat df = shortDateFormat.get();
		if (df == null) {
			df = new SimpleDateFormat("yyMMdd");
			shortDateFormat.set(df);
		}
		return df;
	}

	public static DateFormat getYearMonthFormat() {
		DateFormat df = yearMonthFormat.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyy-MM");
			yearMonthFormat.set(df);
		}
		return df;
	}

	public static DateFormat getYearFormat() {
		DateFormat df = yearFormat.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyy");
			yearFormat.set(df);
		}
		return df;
	}

	public static DateFormat getYMDHMSFormat() {
		DateFormat df = ymdhmsFormat.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyyMMddHHmmss");
			ymdhmsFormat.set(df);
		}
		return df;
	}

	public static NumberFormat getFloatFormat0() {
		NumberFormat nf = floatFormat0.get();
		if (nf == null) {
			nf = new DecimalFormat("#");
			floatFormat0.set(nf);
		}
		return nf;
	}

	public static NumberFormat getFloatFormat1() {
		NumberFormat nf = floatFormat1.get();
		if (nf == null) {
			nf = new DecimalFormat("#.#");
			floatFormat1.set(nf);
		}
		return nf;
	}

	public static NumberFormat getFloatFormat2() {
		NumberFormat nf = floatFormat2.get();
		if (nf == null) {
			nf = new DecimalFormat("#.##");
			floatFormat2.set(nf);
		}
		return nf;
	}

	public static NumberFormat getFloatFormat4() {
		NumberFormat nf = floatFormat4.get();
		if (nf == null) {
			nf = new DecimalFormat("#.####");
			floatFormat4.set(nf);
		}
		return nf;
	}

	public static int getIntFromString(String text) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(text);
		try {
			while (matcher.find()) {
				return Integer.parseInt(matcher.group(0));
			}
		} catch (Exception ex) {
		}
		return 0;
	}
}
