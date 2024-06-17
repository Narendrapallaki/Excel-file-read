package com.example.productcontrollor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.productservice.UserService;

@RestController
public class UserControllor {
	
	
	 @Autowired
	    private UserService userService;

	    @PostMapping("/save")
	    public User saveUser(@RequestBody User user) {
	        User savedUser = userService.saveUser(user);
	        

	        return savedUser;
	    }

}
