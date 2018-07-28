package com.abrahamhan.SecKill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.abrahamhan.SecKill.domain.SecKillGoods;
import com.abrahamhan.SecKill.vo.GoodsVo;


@Mapper
public interface GoodsDao {
	
	//获取全部秒杀商品的全部信息
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.seckill_price from seckill_goods mg left join goods g on mg.goods_id = g.id")
	public List<GoodsVo> listGoodsVo();

	//通过id获取秒杀商品信息
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.seckill_price from seckill_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

	//减少库存,解决卖超
	@Update("update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
	public int reduceStock(SecKillGoods g);

	@Update("update seckill_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
	public int resetStock(SecKillGoods g);
	
}
