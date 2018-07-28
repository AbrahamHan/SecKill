package com.abrahamhan.SecKill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abrahamhan.SecKill.dao.GoodsDao;
import com.abrahamhan.SecKill.domain.SecKillGoods;
import com.abrahamhan.SecKill.vo.GoodsVo;


@Service
public class GoodsService {
	
	@Autowired
	GoodsDao goodsDao;
	
	public List<GoodsVo> listGoodsVo(){
		return goodsDao.listGoodsVo();
	}
	/**
	 * 通过goods的id获取详细goods信息
	 * @param goodsId
	 * @return
	 */
	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}
	/**
	 * 减少库存
	 * @param goods
	 */
	public boolean reduceStock(GoodsVo goods) {
		SecKillGoods g = new SecKillGoods();
		g.setGoodsId(goods.getId());
		int ret = goodsDao.reduceStock(g);
		return ret > 0;
	}

	public void resetStock(List<GoodsVo> goodsList) {
		for(GoodsVo goods : goodsList ) {
			SecKillGoods g = new SecKillGoods();
			g.setGoodsId(goods.getId());
			g.setStockCount(goods.getStockCount());
			goodsDao.resetStock(g);
		}
	}
	
	
}
