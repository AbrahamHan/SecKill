package com.abrahamhan.SecKill.redis;

public class SecKillKey extends BasePrefix{

	private SecKillKey(String prefix) {
		super(prefix);
	}
	public static SecKillKey isGoodsOver = new SecKillKey("go");
}
