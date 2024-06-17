package com.example.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Address;
import com.example.entity.User;
import com.example.repo.AddressRepository;
import com.example.repo.UserRepository;

@Service
public class UserService {
	
	  @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private AddressRepository addressRepository;

	    public User saveUser(User user) {
	        // Save user details
	        User savedUser = userRepository.save(user);

	        // Save associated addresses
//	        for (Address address : user.getAddresses()) {
//	            address.setUser(savedUser);
//	            addressRepository.save(address);
//	        }
	        return savedUser;
	    }

}
