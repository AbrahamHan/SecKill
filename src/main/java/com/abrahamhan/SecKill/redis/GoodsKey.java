package com.abrahamhan.SecKill.redis;
/**
 * 商品key的前缀
 * @author abrahamhan
 *
 */
public class GoodsKey extends BasePrefix {

	public GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	//获取商品列表
	public static GoodsKey getGoodsList = new GoodsKey(60,"gl"); 
	//获取商品详细信息
	public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd"); 
 
}
