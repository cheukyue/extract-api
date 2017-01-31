package us.ceka.util;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeUtilTest {
	
	protected final Logger log = LoggerFactory.getLogger(DateTimeUtilTest.class);
	
	@Test
	public void isFutureMonthNextYearTest() {
		
		int intMonth = 1;
		log.info("executing isFutureMonthNextYearTest with argument value [{}], Current month [{}]...(Expected true)", 
				intMonth, LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM")));
		if("Dec".equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM"))))
			assertTrue(DateTimeUtil.isFutureMonthNextYear(intMonth));
		else 
			assertFalse(DateTimeUtil.isFutureMonthNextYear(intMonth));
		
		String strMonth = "12";
		log.info("executing isFutureMonthNextYearTest with argument value [{}], Current month [{}]...(Expected false)", 
				strMonth, LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM")));
		assertFalse(DateTimeUtil.isFutureMonthNextYear(strMonth));
	}
}
