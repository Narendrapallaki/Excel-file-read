package com.example.repo;

import org.springframework.data.repository.CrudRepository;

import com.example.entity.User;

public interface UserRepository  extends CrudRepository<User, Integer>{

}
