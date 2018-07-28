package com.abrahamhan.SecKill.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abrahamhan.SecKill.dao.OrderDao;
import com.abrahamhan.SecKill.domain.OrderInfo;
import com.abrahamhan.SecKill.domain.SecKillOrder;
import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.redis.OrderKey;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.vo.GoodsVo;


@Service
public class OrderService {
	
	@Autowired
	OrderDao orderDao;
	@Autowired
	RedisService redisService;
	/**
	 * 通过用户id和商品id获取秒杀订单
	 * @param userId
	 * @param goodsId
	 * @return
	 */
	public SecKillOrder getSecKillOrderByUserIdGoodsId(long userId, long goodsId) {
		//return orderDao.getSecKillOrderByUserIdGoodsId(userId, goodsId);
		return redisService.get(OrderKey.getSeckillOrderByUidGid, ""+userId+"_"+goodsId, SecKillOrder.class);
	}
	
	public OrderInfo getOrderById(long orderId) {
		return orderDao.getOrderById(orderId);
	}
	/**
	 * 创建订单
	 * @param user
	 * @param goods
	 * @return
	 */
	@Transactional
	public OrderInfo createOrder(SecKillUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getSeckillPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderDao.insert(orderInfo);
		SecKillOrder secKillOrder = new SecKillOrder();
		secKillOrder.setGoodsId(goods.getId());
		secKillOrder.setOrderId(orderInfo.getId());
		secKillOrder.setUserId(user.getId());
		orderDao.insertSecKillOrder(secKillOrder);
		
		redisService.set(OrderKey.getSeckillOrderByUidGid, ""+user.getId()+"_"+goods.getId(), secKillOrder);
		
		return orderInfo; 
	}

	public void deleteOrders() {
		orderDao.deleteOrders();
		orderDao.deleteMiaoshaOrders();
	}
}
