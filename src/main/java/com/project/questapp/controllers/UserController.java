package com.project.questapp.controllers;

import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.questapp.entities.User;
import com.project.questapp.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private UserService userService; /*UserRepository userRepository= .. diye yazmadık onun yerine constructor ınjection ı kullanıyoruz aşagıda boylelıkle spring  userRespostiroynin  bean ini bulup injection edicek içerisine atıcak
	*/
	public UserController(UserService userService)
	{
		this.userService=userService; //spring boot un getirdiği bean i bizim repomuza atıcak.
		
	}
	
	@GetMapping // () açıp ekstra bir şey belirtmedik /users içinde çalışacak
	public List<User> getAllUser(){
		return userService.getAllUsers();
		
	}
	
	@PostMapping //biri yeni bir user create etmek istediğinde
	public User createUser(@RequestBody User newUser) //parantez içinin anlamı sırasıyla: Gelen istekteki requestteki bodydeki bilgileri alıp User objesine maple, sonra bana o user objesini newUser olarak dön.
	{
		return userService.saveOneUser(newUser); //user objesini database e save et.
	}
	
	@GetMapping("/{userId}")
	public User getOneUser(@PathVariable Long userId) { // parantez ici: pathdeki degiskeni long turunden maple userId olarak döndür
		//custom exception
		return userService.getOneUserById(userId);
	}
	
	@PutMapping("/{userId}")
	public User updateOneUser(@PathVariable Long userId, @RequestBody User newUser) //@requestbody ile update etmek istediğimiz userı alacağız
	{

		return userService.updateOneUser(userId,newUser); //sana bir userId bir newUser veriyorum update islemini yap diyoruz service e
		
	}
	
	@DeleteMapping("/{userId}")
	public void deleteOneUser(@PathVariable Long userId)
	{
		userService.deletOneUser(userId);	
	}
	
	
	

}
