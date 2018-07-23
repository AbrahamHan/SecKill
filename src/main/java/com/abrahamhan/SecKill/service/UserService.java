package com.abrahamhan.SecKill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abrahamhan.SecKill.dao.UserDao;
import com.abrahamhan.SecKill.domain.User;

@Service
public class UserService {
	@Autowired
	UserDao userDao;
	
	public User getById(int id)
	{
		return userDao.getById(id);
	}
}
