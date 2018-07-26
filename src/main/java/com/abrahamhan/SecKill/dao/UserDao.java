package com.abrahamhan.SecKill.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.abrahamhan.SecKill.domain.User;

@Mapper
public interface UserDao {
	//同过id来获取用户
	@Select("Select * from user where id = #{id}")
	public User getById(@Param("id") int id);
}
