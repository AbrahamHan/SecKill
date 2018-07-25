package com.abrahamhan.SecKill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.service.SecKillService;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	SecKillService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/to_list")
    public String list(Model model,SecKillUser user) {
    	model.addAttribute("user", user);
        return "goods_list";
    }
    
}
