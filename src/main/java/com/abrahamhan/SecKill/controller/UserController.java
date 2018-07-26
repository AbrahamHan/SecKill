package com.abrahamhan.SecKill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.domain.User;
import com.abrahamhan.SecKill.redis.UserKey;
import com.abrahamhan.SecKill.result.Result;

@Controller
@RequestMapping("/user")
public class UserController {
	@RequestMapping("/info")
	@ResponseBody
	public Result<SecKillUser> info(Model model,SecKillUser user)
	{
		return Result.success(user);
		
	}
}
