package com.own.bb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.own.bb.dao.UserRepository;
import com.own.bb.entity.User;



@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
	@Autowired
	UserRepository userRepository;

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User update(User user) {
		User result = userRepository.save(user);
		return result;
	}
}
