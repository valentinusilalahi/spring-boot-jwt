package com.silalahi.valentinus.jwt.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.silalahi.valentinus.jwt.exception.CustomException;
import com.silalahi.valentinus.jwt.model.User;
import com.silalahi.valentinus.jwt.repository.UserRepository;
import com.silalahi.valentinus.jwt.security.JwtTokenprovider;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenprovider jwtTokenProvider;
	
	@Autowired
	private AuthenticationManager authentincationManager;
	
	public String signin(String username, String password) {
		try {
			authentincationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
		} catch (AuthenticationException autEx) {
			
			throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
			
		}
	}
	
	public String signup(User user) {
		if(!userRepository.existByUsername(user.getUsername())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
		}else {
			throw new CustomException("Username is aready use", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	public void delete(String username) {
		userRepository.deleteByUsername(username);
	}
	
	public User search(String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			throw new CustomException("The user doesn't exist ", HttpStatus.NOT_FOUND);
		}
		return user;
	}
	
	public User whoami(HttpServletRequest request) {
		return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));
	}

}
