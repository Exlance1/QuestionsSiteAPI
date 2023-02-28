package com.project.questapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.questapp.entities.User;
import com.project.questapp.repos.UserRepository;

@Service
public class UserService {
	
	UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}

	public User saveOneUser(User newUser) {
		
		return userRepository.save(newUser);
	}

	public User getOneUserById(Long userId) {
		return userRepository.findById(userId).orElse(null);
		
	}

	public User updateOneUser(Long userId, User newUser) {
		Optional<User> user=userRepository.findById(userId);
		if(user.isPresent())
		{
			User foundUser=user.get();
			foundUser.setUserName(newUser.getUserName()); //update process
			foundUser.setPassword(newUser.getPassword());
			userRepository.save(foundUser); //save database
			return foundUser;	
		}
		
		else
		{	
			return null; 	
		}
	}

	public void deletOneUser(Long userId) {
		 userRepository.deleteById(userId);
		
	}
	
	

}
