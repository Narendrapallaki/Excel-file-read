package com.example.productcontrollor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Product;
import com.example.productservice.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:4200") 
public class ProductControllor {

	
	
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/api/getproduct")
	public List<Product>getProduct()
	{
		return productService.getProduct();
		
	}
}
