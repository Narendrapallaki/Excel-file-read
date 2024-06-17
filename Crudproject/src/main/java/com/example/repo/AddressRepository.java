package com.example.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Address;
@Repository
public interface AddressRepository extends CrudRepository<Address, Long>{

}
