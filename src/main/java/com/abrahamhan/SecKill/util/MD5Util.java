package com.abrahamhan.SecKill.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	private static final String salt = "1a2b3c4d";
	
	public static String inputPass2FormPass(String inputPass)
	{
		String str = ""+salt.charAt(0)+salt.charAt(2)+inputPass + salt.charAt(5) +salt.charAt(4);
		//System.out.println(str);
		return md5(str);
	}
	
	public static String formPass2DBPass(String formPass ,String salt)
	{
		String str = ""+salt.charAt(0)+salt.charAt(2)+formPass + salt.charAt(5) +salt.charAt(4);
		return md5(str);
	}
	public static String inputPass2DbPass(String input,String saltDB)
	{
		String formPass = inputPass2FormPass(input);
		String dbPass = formPass2DBPass(formPass,saltDB);
		return dbPass;
	}
	public static void main(String []args)
	{
		String pass = "aaaaaa";
		String formPass = inputPass2FormPass(pass);
		System.out.println(formPass);
		System.out.println(formPass2DBPass(formPass,salt));
	}
}
