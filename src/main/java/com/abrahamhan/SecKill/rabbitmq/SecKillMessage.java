package com.abrahamhan.SecKill.rabbitmq;

import com.abrahamhan.SecKill.domain.SecKillUser;

public class SecKillMessage {
	private SecKillUser user;
	private long goodsId;
	public SecKillUser getUser() {
		return user;
	}
	public void setUser(SecKillUser user) {
		this.user = user;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
}
