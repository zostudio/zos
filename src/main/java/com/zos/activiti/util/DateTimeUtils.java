package com.zos.activiti.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * 日期与时间工具类
 * 
 * @author 01Studio
 *
 */
@Component
public class DateTimeUtils {

	/**
	 * 判断现在的时间, 是否在给定的范围之内, 如果参数任何一个为空, 则返回 false
	 * 
	 * @param nowTime 现在时间
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return true|false
	 * @throws Exception
	 */
	public Boolean belongTime(Date nowTime, Date startTime, Date endTime) throws Exception {
		if (null == nowTime || null == startTime || null == endTime) {
			return false;
		}
		if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
			return true;
		}
		// 设置当前时间
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		// 设置开始时间
		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);
		// 设置结束时间
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		// 处于开始时间之后和结束时间之前的判断
		if (date.after(begin) && date.before(end)) {
			return true;
		}
		return false;
	}
}
