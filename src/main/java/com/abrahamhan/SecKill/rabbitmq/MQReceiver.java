package com.abrahamhan.SecKill.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abrahamhan.SecKill.domain.SecKillOrder;
import com.abrahamhan.SecKill.domain.SecKillUser;
import com.abrahamhan.SecKill.redis.RedisService;
import com.abrahamhan.SecKill.service.GoodsService;
import com.abrahamhan.SecKill.service.OrderService;
import com.abrahamhan.SecKill.service.SecKillOrderService;
import com.abrahamhan.SecKill.service.SecKillService;
import com.abrahamhan.SecKill.vo.GoodsVo;


@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		@Autowired
		RedisService redisService;
		
		@Autowired
		GoodsService goodsService;
		
		@Autowired
		OrderService orderService;
		
		@Autowired
		SecKillOrderService seckillService;
		
		@RabbitListener(queues=MQConfig.SECKILL_QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			SecKillMessage mm  = RedisService.stringToBean(message, SecKillMessage.class);
			SecKillUser user = mm.getUser();
			long goodsId = mm.getGoodsId();
			
			GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
	    	int stock = goods.getStockCount();
	    	if(stock <= 0) {
	    		return;
	    	}
	    	//判断是否已经秒杀到了
	    	SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(user.getId(), goodsId);
	    	if(order != null) {
	    		return;
	    	}
	    	//减库存 下订单 写入秒杀订单
	    	 seckillService.seckill(user, goods);
		}
	
//		@RabbitListener(queues=MQConfig.QUEUE)
//		public void receive(String message) {
//			log.info("receive message:"+message);
//		}
//		
//		@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
//		public void receiveTopic1(String message) {
//			log.info(" topic  queue1 message:"+message);
//		}
//		
//		@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
//		public void receiveTopic2(String message) {
//			log.info(" topic  queue2 message:"+message);
//		}
//		
//		@RabbitListener(queues=MQConfig.HEADER_QUEUE)
//		public void receiveHeaderQueue(byte[] message) {
//			log.info(" header  queue message:"+new String(message));
//		}
//		
		
}
