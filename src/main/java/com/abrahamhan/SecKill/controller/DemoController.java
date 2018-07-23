package com.abrahamhan.SecKill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abrahamhan.SecKill.domain.User;
import com.abrahamhan.SecKill.result.CodeMsg;
import com.abrahamhan.SecKill.result.Result;
import com.abrahamhan.SecKill.service.UserService;

@Controller

public class DemoController {
	
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/")
	@ResponseBody
	String home()
	{
		return "Hello,world!";
	}
	//1、rest api json输出
	@RequestMapping("/hello")
	@ResponseBody
	public Result<String> hello()
	{
		return Result.success("Hello,Abraham");
	}
	
	@RequestMapping("/helloError")
	@ResponseBody
	public Result<String> helloError()
	{
		return Result.error(CodeMsg.SERVER_ERROR);
		
	}
	//2、页面
	@RequestMapping("/thymeleaf")
	public String thymeleaf(Model model)
	{
		model.addAttribute("name", "AbrahamHan");
		return "hello";
	}
	
	

	 
}
