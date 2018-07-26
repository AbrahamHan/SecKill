package com.abrahamhan.SecKill.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abrahamhan.SecKill.dao.SecKillUserDao;
import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.exception.GlobalException;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.redis.SecKillUserKey;
import com.abrahamhan.SecKill.result.CodeMsg;
import com.abrahamhan.SecKill.util.MD5Util;
import com.abrahamhan.SecKill.util.UUIDUtil;
import com.abrahamhan.SecKill.vo.LoginVo;

@Service
public class SecKillService {
	
	public static final String COOKIE_NAME_TOKEN = "token";
	
	@Autowired
	SecKillUserDao secKillUserDao;
	
	@Autowired
	RedisService redisService;
	
	public SecKillUser getById(long id)
	{
		return secKillUserDao.getById(id);
	}
	/**
	 * 登录前的操作，判断，添加cookie
	 * @param response
	 * @param loginVo
	 * @return
	 */
	public boolean login(HttpServletResponse response,LoginVo loginVo) {
		if(loginVo == null)
		{
			throw new GlobalException( CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机是否存在
		SecKillUser user = getById(Long.parseLong(mobile));
	
		if(user == null)
		{
			throw new GlobalException( CodeMsg.MOBILE_NOT_EXIST);
		}
		
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calPass = MD5Util.formPass2DBPass(formPass, saltDB);
		if(calPass.equals(dbPass) == false)
		{
			throw new GlobalException( CodeMsg.PASSWORD_ERROR);
		}
		//生成一个cookie
		String token = UUIDUtil.uuid();
		addCookie(response,token,user);
		
		return true;
	}
	public SecKillUser getByToken(HttpServletResponse response,String token)
	{
		if(StringUtils.isEmpty(token))
		{
			return null;
		}
		SecKillUser user = redisService.get(SecKillUserKey.token, token, SecKillUser.class);
		//延长缓存期
		if(user != null)
		{
			addCookie(response,token,user);
		}
		return user;
	}

	private void addCookie(HttpServletResponse response, String token, SecKillUser user) {
		redisService.set(SecKillUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
		cookie.setMaxAge(SecKillUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}

