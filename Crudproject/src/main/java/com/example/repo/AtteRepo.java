package com.example.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Attendance;
@Repository
public interface AtteRepo extends CrudRepository<Attendance, Long>{

}
