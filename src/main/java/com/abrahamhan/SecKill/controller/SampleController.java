package com.abrahamhan.SecKill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abrahamhan.SecKill.domain.User;
import com.abrahamhan.SecKill.rabbitmq.MQSender;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.redis.UserKey;
import com.abrahamhan.SecKill.result.Result;
import com.abrahamhan.SecKill.service.UserService;

@Controller
@RequestMapping("/demo")
public class SampleController {
	@Autowired
	UserService userService;
	@Autowired
	RedisService redisService;
	
	@Autowired
	MQSender sender;
	
//	@RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> header() {
//		sender.sendHeader("hello,Abraham");
//        return Result.success("Hello，world");
//    }
//	
//	@RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanout() {
//		sender.sendFanout("hello,Abraham");
//        return Result.success("Hello，world");
//    }
//	
//	@RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topic() {
//		sender.sendTopic("hello,Abraham");
//        return Result.success("Hello，world");
//    }
//	
//	@RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq() {
//		sender.send("hello,Abraham");
//        return Result.success("Hello，world");
//    }
	
	@RequestMapping("/db/get")
	@ResponseBody
	public Result<User> dbGet()
	{
		User user = userService.getById(1);
		return Result.success(user);
		
	}
	@RequestMapping("/redis/get")
	@ResponseBody
	public Result<User> redisGet()
	{
		User user = redisService.get(UserKey.getById,"1", User.class);
		return Result.success(user);
		
	}
	
	@RequestMapping("/redis/set")
	@ResponseBody
	public Result<Boolean> redisSet()
	{
		User user = new User();
		user.setId(1);
		user.setName("11111");
		redisService.set(UserKey.getById, ""+1, user);
		return Result.success(true);
		
	}
	
}
