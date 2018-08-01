package com.abrahamhan.SecKill.redis;

public class SecKillKey extends BasePrefix{

	private SecKillKey(int es,String prefix) {
		super(es,prefix);
	}
	public static SecKillKey isGoodsOver = new SecKillKey(0,"go");
	public static SecKillKey getSeckillPath = new SecKillKey(60,"sp");
	public static SecKillKey getSecKillVerifyCode = new SecKillKey(600,"svc");
}
