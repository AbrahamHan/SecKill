package com.abrahamhan.SecKill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abrahamhan.SecKill.domain.OrderInfo;
import com.abrahamhan.SecKill.domain.SecKillOrder;
import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.result.CodeMsg;
import com.abrahamhan.SecKill.result.Result;
import com.abrahamhan.SecKill.service.GoodsService;
import com.abrahamhan.SecKill.service.OrderService;
import com.abrahamhan.SecKill.service.SecKillOrderService;
import com.abrahamhan.SecKill.service.SecKillService;
import com.abrahamhan.SecKill.vo.GoodsVo;


@Controller
@RequestMapping("/seckill")
public class SecKillController {

	@Autowired
	SecKillService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SecKillOrderService seckillOrderService;
	/**
	 * 秒杀页面
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
    @RequestMapping(value="/do_seckill", method=RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(Model model,SecKillUser user,
    		@RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	//判断库存
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);//10个商品，req1 req2
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
    		return Result.error(CodeMsg.SEC_KILL_OVER);
    	}
    	//判断是否已经秒杀到了
    	SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		return Result.error(CodeMsg.REPEATE_SEC_KILL);
    	}
    	//减库存 下订单 写入秒杀订单
    	OrderInfo orderInfo = seckillOrderService.seckill(user, goods);
        return Result.success(orderInfo);
    }
}
