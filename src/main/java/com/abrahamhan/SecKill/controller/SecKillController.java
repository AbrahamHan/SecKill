package com.abrahamhan.SecKill.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abrahamhan.SecKill.access.AccessLimit;
import com.abrahamhan.SecKill.domain.SecKillOrder;
import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.rabbitmq.MQSender;
import com.abrahamhan.SecKill.rabbitmq.SecKillMessage;
import com.abrahamhan.SecKill.redis.GoodsKey;
import com.abrahamhan.SecKill.redis.OrderKey;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.redis.SecKillKey;
import com.abrahamhan.SecKill.result.CodeMsg;
import com.abrahamhan.SecKill.result.Result;
import com.abrahamhan.SecKill.service.GoodsService;
import com.abrahamhan.SecKill.service.OrderService;
import com.abrahamhan.SecKill.service.SecKillOrderService;
import com.abrahamhan.SecKill.service.SecKillService;
import com.abrahamhan.SecKill.vo.GoodsVo;


@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean{

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
	
	@Autowired
	MQSender sender;
	
	private HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();
	private static Logger log = LoggerFactory.getLogger(SecKillController.class);
    /**
     * 系统初始化
     * @throws Exception
     */
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo>goodsList = goodsService.listGoodsVo();
		
		if(goodsList == null)
		{
			return;
		}
		for(GoodsVo goods:goodsList)
		{
			redisService.set(GoodsKey.getSecKillGoodsStock, ""+goods.getId(), goods.getGoodsStock());
			
			log.info("put:"+goods.getId()+",value:"+false);
			localOverMap.put(goods.getId(), false);
		}
	}
	
	
	/*
	 * 重置
	 */
	@RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		for(GoodsVo goods : goodsList) {
			goods.setStockCount(10);
			redisService.set(GoodsKey.getSecKillGoodsStock, ""+goods.getId(), 10);
			localOverMap.put(goods.getId(), false);
		}
		redisService.delete(OrderKey.getSeckillOrderByUidGid);
		redisService.delete(SecKillKey.isGoodsOver);
		seckillOrderService.reset(goodsList);
		return Result.success(true);
	}
	
	
	/**
	 * 秒杀页面
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
    @RequestMapping(value="/{path}/do_seckill", method=RequestMethod.POST)
    @ResponseBody
    public Result<Integer> seckill(Model model,SecKillUser user,
    		@RequestParam("goodsId")long goodsId,
    		@PathVariable ("path") String path) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	//验证path
    	boolean checkPath = seckillOrderService.checkPath(user,goodsId,path);
    	if(!checkPath)
    	{
    		return Result.error(CodeMsg.REQUEST_ILLEGAL);
    	}
    	//内存标记，减少redis访问
    	boolean over = localOverMap.get(goodsId);
    	if(over) {
    		return Result.error(CodeMsg.SEC_KILL_OVER1);
    	}
    	
    	//预减库存
    	long stock = redisService.decr(GoodsKey.getSecKillGoodsStock, ""+goodsId);//10
    	if(stock < 0) {
    		 localOverMap.put(goodsId, true);
    		return Result.error(CodeMsg.SEC_KILL_OVER2);
    	}
    	//判断是否已经秒杀到了
    	SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		return Result.error(CodeMsg.REPEATE_SEC_KILL);
    	}
    	//入队
    	SecKillMessage mm = new SecKillMessage();
    	mm.setUser(user);
    	mm.setGoodsId(goodsId);
    	sender.sendSecKillMessage(mm);;
    	return Result.success(0);//排队中
    }
    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillPath(SecKillUser user,
    		@RequestParam("goodsId")long goodsId,
    		@RequestParam(value="verifyCode", defaultValue="0") int verifyCode) {
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	boolean check = seckillOrderService.checkVerifyCode(user, goodsId, verifyCode);
    	if(!check) {
    		return Result.error(CodeMsg.REQUEST_ILLEGAL);
    	}
    	String path = seckillOrderService.createSeckillPath(user, goodsId);
    	
		return Result.success(path);
    }
    
    @RequestMapping(value="/verifyCode", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillVerifyCode(HttpServletResponse response,SecKillUser user,
    		@RequestParam("goodsId")long goodsId) {
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	try {
    		BufferedImage image  = seckillOrderService.createVerifyCode(user, goodsId);
    		OutputStream out = response.getOutputStream();
    		ImageIO.write(image, "JPEG", out);
    		out.flush();
    		out.close();
    		return null;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return Result.error(CodeMsg.SEC_KILL_FAIL);
    	}
    }
    /**
     * orderId：成功
     * -1：库存不足，秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,SecKillUser user,
    		@RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	long result  = seckillOrderService.getSecKillResult(user.getId(), goodsId);
    	return Result.success(result);
    }
    

}



