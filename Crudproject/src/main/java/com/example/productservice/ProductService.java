package com.example.productservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Address;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.repo.AddressRepository;
import com.example.repo.UserRepository;

@Service
public class ProductService {
	
	
	
	public List<Product>getProduct()
	{
		ArrayList<Product> pro=new ArrayList<>();
		pro.add(new Product(1, "bottle", 23.20));
		pro.add(new Product(2, "apple", 100.20));
		pro.add(new Product(3, "cap", 250.20));
		pro.add(new Product(4, "box", 235.20));
		
		return pro;
	}
	
	
	 @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private AddressRepository addressRepository;

	    public User saveUser(User user) {
	        // Save user details
	        User savedUser = userRepository.save(user);

	        // Save associated addresses
	        for (Address address : user.getAddresses()) {
	            address.setUser(savedUser);
	            addressRepository.save(address);
	        }

	        return savedUser;
	    }

}
