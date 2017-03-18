package com.bookstore.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.PasswordResetTokenRespository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.UserService;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordResetTokenRespository passwordResetTokenRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public PasswordResetToken gePasswordResetToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenforUser(User user, String token) {
		final PasswordResetToken myToken =new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		
		User localUser=userRepository.findByUsername(user.getUsername());
		
		if(localUser!=null) {
			throw new Exception("User Exitst");
		}else {
			for(UserRole usr:userRoles) {
				roleRepository.save(usr.getRole());
			}
			
			user.getUserRoles().addAll(userRoles);
			localUser=userRepository.save(user);
		}
		return localUser;
	}

}
