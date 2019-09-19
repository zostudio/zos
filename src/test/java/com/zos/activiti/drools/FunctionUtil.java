package com.zos.activiti.drools;

import java.util.Calendar;
import java.util.Date;

public class FunctionUtil {
	
	public static Date plusDay(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}
}
