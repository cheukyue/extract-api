package us.ceka.util;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import org.apache.commons.lang3.math.NumberUtils;

public class DateTimeUtil {
	
    /**
     * Check if current month is December, return true when the argument is 1
     */
	public static boolean isFutureMonthNextYear(Object month) {
		if(month == null) return false;
		
		int checkMonth = -1;
		if(month instanceof String) checkMonth = NumberUtils.toInt((String)month);
		if(month instanceof Integer) checkMonth = ((Integer)month).intValue();
		if(checkMonth == 1) {
			if("Dec".equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM")))) return true; 
		}
		return false;
	}
	
	public static LocalDateTime correctDateWithChineseDayOfWeek(LocalDateTime date, String chineseDayOfWeek) throws ParseException {
		
		DayOfWeek checkDay = date.getDayOfWeek();
		DayOfWeek correctDay = null;
		
		if(checkDay.getDisplayName(TextStyle.FULL, Locale.TRADITIONAL_CHINESE).equals(chineseDayOfWeek)) return date;
		
		for(DayOfWeek d : DayOfWeek.values()) {
			if(d.getDisplayName(TextStyle.FULL, Locale.TRADITIONAL_CHINESE).equals(chineseDayOfWeek)) correctDay = d;
		}
			
		if(correctDay == null) throw new ParseException("Unable to parse chinese dayOfWeek in argument 1", 0);

		int dayDiff = correctDay.getValue() - checkDay.getValue();
		if(Math.abs(dayDiff) > 1) dayDiff = dayDiff >= 0 ? dayDiff - 7 : dayDiff + 7;
		
		return date.plusDays(dayDiff);
	}

}
