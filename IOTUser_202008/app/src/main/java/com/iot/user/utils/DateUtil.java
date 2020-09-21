/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.iot.user.utils;

import android.text.format.DateFormat;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间转换工具类
 * @version 4.0
 */
public class DateUtil {
	
	public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
	public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat sformat3 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	public static final SimpleDateFormat sformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

	private static final long ONEDAY = 86400000;
	public static final int SHOW_TYPE_SIMPLE = 0;
	public static final int SHOW_TYPE_COMPLEX = 1;
	public static final int SHOW_TYPE_ALL = 2;
	public static final int SHOW_TYPE_CALL_LOG = 3;
	public static final int SHOW_TYPE_CALL_DETAIL = 4;

	/**
	 * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
	 *
	 * @return
	 */
	public static long getCurrentDayTime() {
		Date d = new Date(System.currentTimeMillis());
		String formatDate = yearFormat.format(d);
		try {
			return (yearFormat.parse(formatDate)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 将long日期转为年月日时分秒
	 * @return
	 */
	public static String getYearDayTime(Long time) {
		return getYearDayTime(String.valueOf(time));
	}

	/**
	 * 将long日期转为年月日时分秒
	 * @return
	 */
	public static String getYearDayTime2(Long time) {
		return getYearDayTime2(String.valueOf(time));
	}

	/**
	 * 将long日期转为年月日
	 * @return
	 */
	public static String getYearTime(Object time) {
		return getYearDay(String.valueOf(time));
	}

	/**
	 * 将long日期转为年月日 时分秒
	 * @return
	 */
	public static String getYearDayTime(String time) {
		try {
			long longtime = Long.valueOf(time);
			if(longtime<=0){
				return "";
			}
			if(time.length()==13){
				Date d = new Date(Long.valueOf(time));
				return sformat.format(d);
			}
			Date d = new Date(Long.valueOf(time)* 1000L);
			return sformat.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将long日期转为年月日 时分
	 * @return
	 */
	public static String getYearDayTime2(String time) {
		try {
			long longtime = Long.valueOf(time);
			if(longtime<=0){
				return "";
			}
			if(time.length()==13){
				Date d = new Date(Long.valueOf(time));
				return sformat3.format(d);
			}
			Date d = new Date(Long.valueOf(time)* 1000L);
			return sformat3.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将long日期转为年月日
	 * @return
	 */
	public static String getYearDay(String time) {
		try {
			if(time.length()==13){
				Date d = new Date(Long.valueOf(time));
				return yearFormat.format(d);
			}
			Date d = new Date(Long.valueOf(time)* 1000L);
			return yearFormat.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static long getChooseDayTime(String date) {
		try {
			return (yearFormat.parse(date)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

    public static String formatDate(int year, int month, int day) {
        Date d = new Date(year - 1900, month, day);
        return yearFormat.format(d);
    }
    public static String formatNowDate(Date date) {
    	return format.format(date);
    }
    public static String sFormatNowDate(Date date) {
    	return sformat.format(date);
    }



    public static String sFormatNowDate2(Date date) {

		String s = sformat2.format(date);

    	return s.substring(0,s.lastIndexOf("+"));
    }

    public static long getDateMills(int year, int month, int day) {
        //Date d = new Date(year, month, day);
		// 1960 4 22
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month, day);
		TimeZone tz = TimeZone.getDefault();
		calendar.setTimeZone(tz);
        return calendar.getTimeInMillis();
    }
	
	public static String getDateString(long time, int type) {
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance(tz);
		c.setTimeInMillis(time);
		long currentTime = System.currentTimeMillis();
		Calendar current_c = Calendar.getInstance();
		current_c = Calendar.getInstance(tz);
		current_c.setTimeInMillis(currentTime);

		int currentYear = current_c.get(Calendar.YEAR);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		long t = currentTime - time;
		long t2 = currentTime - getCurrentDayTime();
		String dateStr = "";
		if (t < t2 && t > 0) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = IOTApplication.getIntstance().getString(R.string.common_today)+"  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = IOTApplication.getIntstance().getString(R.string.common_today)+"  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = IOTApplication.getIntstance().getString(R.string.common_today)+"  ";
			}else {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (t < (t2 + ONEDAY) && t > 0) {
			if (type == SHOW_TYPE_SIMPLE || type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = IOTApplication.getIntstance().getString(R.string.common_yesterday)+"  ";
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = IOTApplication.getIntstance().getString(R.string.common_yesterday)+"  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = IOTApplication.getIntstance().getString(R.string.common_yesterday)+"  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else {
				dateStr = IOTApplication.getIntstance().getString(R.string.common_yesterday)+"  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (y == currentYear) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + IOTApplication.getIntstance().getString(R.string.common_month) + (d < 10 ? "0" + d : d)
						+ IOTApplication.getIntstance().getString(R.string.common_day);
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + /* 月 */"/"
						+ (d < 10 ? "0" + d : d) + /* 日 */" "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = (m < 10 ? "0" + m : m) + IOTApplication.getIntstance().getString(R.string.common_month) + (d < 10 ? "0" + d : d)
						+ IOTApplication.getIntstance().getString(R.string.common_day) + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = y + IOTApplication.getIntstance().getString(R.string.common_year) + (m < 10 ? "0" + m : m) + IOTApplication.getIntstance().getString(R.string.common_month)
						+ (d < 10 ? "0" + d : d) + IOTApplication.getIntstance().getString(R.string.common_day);
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = y + /* 年 */"/" + (m < 10 ? "0" + m : m) + /* 月 */"/"
						+ (d < 10 ? "0" + d : d) + /* 日 */"  "/*
																 * + (hour < 10
																 * ? "0" + hour
																 * : hour) + ":"
																 * + (minute <
																 * 10 ? "0" +
																 * minute :
																 * minute)
																 */;
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = y + IOTApplication.getIntstance().getString(R.string.common_year) + (m < 10 ? "0" + m : m) + IOTApplication.getIntstance().getString(R.string.common_month)
						+ (d < 10 ? "0" + d : d) + IOTApplication.getIntstance().getString(R.string.common_day)
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		}
		return dateStr;
	}

	/**
	 * 
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis() / 1000;
	}

    public static long getActiveTimelong(String result) {
        try {
            Date parse = yearFormat.parse(result);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }


	public static final String DEFAULT_FORMAT = "yyyy-MM-dd kk:mm:ss";
	public static final String DEFAULT_FORMAT_MS = "yyyy-MM-dd kk:mm:ss.SSS";
	public static final String DEFAULT_FORMAT_DAY = "yyyy-MM-dd";
	public static String getNow(String format){
		SimpleDateFormat simpleformat = new SimpleDateFormat(format);
		return simpleformat.format(new Date(System.currentTimeMillis()));
	}
	public static String getDate(String format, Long timestamp){
		Date date = new Date(timestamp);
		SimpleDateFormat simpleformat = new SimpleDateFormat(format);
		//return DateFormat.format(format, timestamp).toString();
		return simpleformat.format(date);
	}

	public static String getDate(String format, Date date){
		return DateFormat.format(format, date).toString();
	}

	public static Date parseDate(String format, String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static String getSimpleTime(Long timestamp){
		if(getNow("yyyy-MM-dd").equals(getDate("yyyy-MM-dd", timestamp))){
			return getDate("kk:mm", timestamp);
		}
		if(System.currentTimeMillis() - timestamp < 86400 * 1000 * 2l - timestamp % 86400000l){
			return IOTApplication.getIntstance().getString(R.string.common_yesterday);
		}
		if(System.currentTimeMillis() - timestamp < 86400 * 1000 * 7l - timestamp % 86400000l){
			return getChineseWeek(new Date(timestamp).getDay());
		}
		return getDate("yyyy-MM-dd", timestamp);
	}


	/**
	 * 获取星期值
	 * @param dayOfWeek
	 * @return
	 */
	public static String getChineseWeek(int dayOfWeek){
		switch (dayOfWeek) {
			case 0:
				return IOTApplication.getIntstance().getString(R.string.common_sunday);
			case 1:
				return IOTApplication.getIntstance().getString(R.string.common_monday);
			case 2:
				return IOTApplication.getIntstance().getString(R.string.common_tuesday);
			case 3:
				return IOTApplication.getIntstance().getString(R.string.common_wednesday);
			case 4:
				return IOTApplication.getIntstance().getString(R.string.common_thursday);
			case 5:
				return IOTApplication.getIntstance().getString(R.string.common_friday);
			case 6:
				return IOTApplication.getIntstance().getString(R.string.common_saturday);
			default:
				return IOTApplication.getIntstance().getString(R.string.common_monday);
		}
	}

	public static String getTimeLength(String timeStr) {
		StringBuilder sb = new StringBuilder();
		String[] arr = timeStr.split(":");
		Integer[] intArr = new Integer[arr.length];
		for(int i = 0; i < arr.length; ++i){
			intArr[i] = Integer.parseInt(arr[i]);
		}
		if(arr.length == 3){
			sb.append(intArr[arr.length - 3]);
			sb.append(IOTApplication.getIntstance().getString(R.string.common_hour));
		}
		if(intArr[arr.length - 2] != 0){
			sb.append(intArr[arr.length - 2]);
			sb.append(IOTApplication.getIntstance().getString(R.string.common_minute));
		}
		sb.append(intArr[arr.length - 1]);
		sb.append(IOTApplication.getIntstance().getString(R.string.common_second));
		return sb.toString();
	}

	public static String secToTimeStr(int secValue){
		int hour = secValue / 3600;
		int min =  secValue % 3600 / 60;
		int sec =  secValue % 60;
		StringBuilder timeStrSb = new StringBuilder();
		if(hour != 0){
			timeStrSb.append((hour < 10 ? "0" + hour : hour) + ":");
		}
		timeStrSb.append((min < 10 ? "0" + min : min) + ":");
		timeStrSb.append(sec < 10 ? "0" + sec : sec);
		return " "+timeStrSb.toString();
	}

	public static boolean timeValidate(long nowTime, String startTime, int days){
		long start = parseDate(DEFAULT_FORMAT, startTime).getTime();
		long deadTime = start + days * 86400L * 1000L;
		return nowTime < deadTime;
	}

	public static String formatSec(int sec){
		String hh=sec/3600>9?sec/3600+"":"0"+sec/3600;
		String  mm=(sec % 3600)/60>9?(sec % 3600)/60+"":"0"+(sec % 3600)/60;
		String ss=(sec % 3600) % 60>9?(sec % 3600) % 60+"":"0"+(sec % 3600) % 60;
		if(hh.equals("00")){
			return mm+":"+ss;
		}else {
			return hh + ":" + mm + ":" + ss;
		}
	}

	public static int timeStrToSec(String timeStr)
	{
		int timeSec = 0;
		if(null != timeStr && !timeStr.equals(""))
		{
			String [] string = timeStr.split(":");
			for (int i = string.length-1;i>=0 ;i--)
			{
				String str = string[i];
				int time = Integer.valueOf(str);
				if (i == (string.length -1))
				{
					timeSec = timeSec+time;
				}else if (i == (string.length -2))
				{
					timeSec = timeSec+(time*60);
				}else if (i == (string.length -3))
				{
					timeSec = timeSec+(time*60*60);
				}
			}
		}
		return  timeSec;
	}

}
