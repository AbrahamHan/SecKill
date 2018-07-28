package com.abrahamhan.SecKill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abrahamhan.SecKill.domain.OrderInfo;
import com.abrahamhan.SecKill.domain.SecKillOrder;
import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.redis.SecKillKey;
import com.abrahamhan.SecKill.vo.GoodsVo;


@Service
public class SecKillOrderService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	RedisService redisService;
	/**
	 * 秒杀事务
	 * @param user
	 * @param goods
	 * @return
	 */
	@Transactional
	public OrderInfo seckill(SecKillUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		boolean suc  = goodsService.reduceStock(goods);
		if (suc)
		{		
			//order_info seckill_order
			return orderService.createOrder(user, goods);
		}
		else
		{
			setGoodsOver(goods.getId());
			return null;
		}
		
	}
	public long getSecKillResult(Long userId, long goodsId) {
		SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(userId, goodsId);
		if(order != null) {//秒杀成功
			return order.getOrderId();
		}else {
			boolean isOver = getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			}else {
				return 0;
			}
		}
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(SecKillKey.isGoodsOver, ""+goodsId, true);
	}
	
	private boolean getGoodsOver(long goodsId) {
		return redisService.exists(SecKillKey.isGoodsOver, ""+goodsId);
	}
	
	public void reset(List<GoodsVo> goodsList) {
		goodsService.resetStock(goodsList);
		orderService.deleteOrders();
	}
	
}
