package com.abrahamhan.SecKill.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.abrahamhan.SecKill.domain.SecKillUser;

@Mapper
public interface SecKillUserDao {
	
	@Select("select * from seckill_user where id = #{id}")
	public SecKillUser getById(@Param("id")long id);
}
