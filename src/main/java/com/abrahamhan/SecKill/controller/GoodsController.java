package com.abrahamhan.SecKill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.service.GoodsService;
import com.abrahamhan.SecKill.service.SecKillService;
import com.abrahamhan.SecKill.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	SecKillService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/to_list")
    public String list(Model model,SecKillUser user) {
    	model.addAttribute("user", user);
    	//查询商品列表
    	List<GoodsVo> goodsList = goodsService.listGoodsVo();
    	model.addAttribute("goodsList",goodsList);
    	
        return "goods_list";
    }
    
    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model,SecKillUser user,
    		@PathVariable("goodsId") long goodsId) { 
    	model.addAttribute("user", user);
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	model.addAttribute("goods",goods);
    	
    	long startAt = goods.getStartDate().getTime();
    	long endAt = goods.getEndDate().getTime();
    	long now = System.currentTimeMillis();
    	int remainSeconds = 0;
    	int seckillStatus = 0;
    	
    	if(now < startAt)
    	{
    		seckillStatus = 0 ;
    		remainSeconds = (int)((startAt-now)/1000);
    	}
    	else if(now > endAt)
    	{
    		seckillStatus = 2;
    		remainSeconds = -1;
    	}
    	else
    	{
    		seckillStatus = 1;
    		remainSeconds = 0;
    	}
    	
    	model.addAttribute("seckillStatus",seckillStatus);
    	model.addAttribute("remainSeconds", remainSeconds);
    	
        return "goods_detail";
    }
    
}
