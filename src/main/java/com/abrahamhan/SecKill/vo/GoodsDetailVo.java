package com.abrahamhan.SecKill.vo;

import com.abrahamhan.SecKill.domain.SecKillUser;

public class GoodsDetailVo {
	private int seckillStatus = 0;
	private int remainSeconds = 0;
	private GoodsVo goods ;
	private SecKillUser user;
	public int getSeckillStatus() {
		return seckillStatus;
	}
	public void setSeckillStatus(int seckillStatus) {
		this.seckillStatus = seckillStatus;
	}
	public int getRemainSeconds() {
		return remainSeconds;
	}
	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}
	public GoodsVo getGoods() {
		return goods;
	}
	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}
	public SecKillUser getUser() {
		return user;
	}
	public void setUser(SecKillUser user) {
		this.user = user;
	}
}
