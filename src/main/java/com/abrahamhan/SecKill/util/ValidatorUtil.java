package com.abrahamhan.SecKill.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidatorUtil {
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
	/**
	 * 判断电话号码是否符合规范
	 * @param src
	 * @return
	 */
	public static boolean isMobile(String src)
	{
		if(StringUtils.isEmpty(src))
		{
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}
}
