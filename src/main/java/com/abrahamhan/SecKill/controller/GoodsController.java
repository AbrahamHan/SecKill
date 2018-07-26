package com.abrahamhan.SecKill.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.redis.GoodsKey;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.result.Result;
import com.abrahamhan.SecKill.service.GoodsService;
import com.abrahamhan.SecKill.service.SecKillService;
import com.abrahamhan.SecKill.vo.GoodsDetailVo;
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
	
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	
	@Autowired
	ApplicationContext applicationConText;
	
	
	/**
	 * 商品列表页面
	 * 未加缓存前的QPS:1024(open files : 1024,2000 threads,10 loops)
	 * 加入缓存后的QPS:8764(open files : 4096,2000 threads,10 loops)
	 * @param request
	 * @param response
	 * @param model
	 * @param user
	 * @return
	 */
    @RequestMapping(value = "/to_list",produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request,HttpServletResponse response,Model model,SecKillUser user) {
    	model.addAttribute("user", user);
    	//取缓存
    	String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
    	if(!StringUtils.isEmpty(html))
    	{
    		return html;
    	}
    	//查询商品列表
    	List<GoodsVo> goodsList = goodsService.listGoodsVo();
    	model.addAttribute("goodsList",goodsList);

    	SpringWebContext context = new SpringWebContext(request,response,request.getServletContext(),
    			request.getLocale(),model.asMap(),applicationConText );
		//手动渲染
    	html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
    	if(!StringUtils.isEmpty(html))
    	{
    		redisService.set(GoodsKey.getGoodsList, "", html);
    	}
    	
        return html;
    }
    
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model,SecKillUser user,
    		@PathVariable("goodsId")long goodsId) {
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	long startAt = goods.getStartDate().getTime();
    	long endAt = goods.getEndDate().getTime();
    	long now = System.currentTimeMillis();
    	int seckillStatus = 0;
    	int remainSeconds = 0;
    	if(now < startAt ) {//秒杀还没开始，倒计时
    		seckillStatus = 0;
    		remainSeconds = (int)((startAt - now )/1000);
    	}else  if(now > endAt){//秒杀已经结束
    		seckillStatus = 2;
    		remainSeconds = -1;
    	}else {//秒杀进行中
    		seckillStatus = 1;
    		remainSeconds = 0;
    	}
    	GoodsDetailVo vo = new GoodsDetailVo();
    	vo.setGoods(goods);
    	vo.setUser(user);
    	vo.setRemainSeconds(remainSeconds);
    	vo.setSeckillStatus(seckillStatus);
    	return Result.success(vo);
    }
    
    /**
     * 商品详细信息页面
     * @param request
     * @param response
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value="/to_detail2/{goodsId}",produces="text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request,HttpServletResponse response,Model model,SecKillUser user,
    		@PathVariable("goodsId") long goodsId) { 
    	model.addAttribute("user", user);
    	
    	//取缓存
    	String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
    	if(!StringUtils.isEmpty(html))
    	{
    		return html;
    	}
    	
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
    	

    	SpringWebContext context = new SpringWebContext(request,response,request.getServletContext(),
    			request.getLocale(),model.asMap(),applicationConText );
		//手动渲染
    	html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
    	if(!StringUtils.isEmpty(html))
    	{
    		redisService.set(GoodsKey.getGoodsDetail,  ""+goodsId, html);
    	}
    	
        return html;
    }
    
}
