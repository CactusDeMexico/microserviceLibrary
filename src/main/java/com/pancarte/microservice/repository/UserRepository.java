package com.pancarte.microservice.repository;


import com.pancarte.microservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findById(int id);
    @Query(value = "SELECT * FROM user1 u WHERE u.iduser > 0",nativeQuery = true)
    List<User> findAll();

}
