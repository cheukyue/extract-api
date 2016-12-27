package us.ceka.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

}
