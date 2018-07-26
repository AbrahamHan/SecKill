package com.abrahamhan.SecKill.redis;
/**
 * 用户Key的前缀类
 * @author abrahamhan
 *
 */
public class UserKey extends BasePrefix {

	
	public UserKey( String prefix) {
		super( prefix);
	}
	
	public UserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	//通过id来获取UserKey
	public static UserKey getById = new UserKey("id");
	//通过name来获取UserKey
	public static UserKey getByName = new UserKey("name");

}
