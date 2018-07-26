package com.abrahamhan.SecKill.util;

import java.util.UUID;

public class UUIDUtil {
	/**
	 * 生成uuid
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
