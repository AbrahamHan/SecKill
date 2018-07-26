package com.abrahamhan.SecKill.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.result.CodeMsg;
import com.abrahamhan.SecKill.result.Result;
import com.abrahamhan.SecKill.service.SecKillService;
import com.abrahamhan.SecKill.service.UserService;
import com.abrahamhan.SecKill.util.ValidatorUtil;
import com.abrahamhan.SecKill.vo.LoginVo;
@Controller
@RequestMapping("/login")
public class LoginController {
	
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	SecKillService secKillService;
	@Autowired
	RedisService redisService;
	
	/**
	 * 登录页面
	 * @return
	 */
	@RequestMapping("/to_login")
	public String toLogin()
	{
		return "login";
	}
	
	
	/**
	 * 页面操作
	 * @param response
	 * @param loginVo
	 * @return
	 */
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<Boolean> do_login(HttpServletResponse response,@Valid LoginVo loginVo)
	{
		log.info(loginVo.toString());
		//登录
		boolean cm = secKillService.login(response,loginVo);
		return Result.success(true);
	}

}
