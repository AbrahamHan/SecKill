package com.abrahamhan.SecKill.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.abrahamhan.SecKill.domain.SecKillUser;

@Mapper
public interface SecKillUserDao {
	
	//查找秒杀用户
	@Select("select * from seckill_user where id = #{id}")
	public SecKillUser getById(@Param("id")long id);
	
	//更新秒杀用户密码
	@Update("update seckill_user set password = #{password} where id = #{id}")
	public void update(SecKillUser toBeUpdate);
	
}
