package com.example.springvaultdemo1.repo;

import com.example.springvaultdemo1.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/************************
 * Made by [MR Ferry™]  *
 * on April 2022        *
 ************************/

@Repository
public interface ChildRepo extends JpaRepository<Child, Integer>{
}
