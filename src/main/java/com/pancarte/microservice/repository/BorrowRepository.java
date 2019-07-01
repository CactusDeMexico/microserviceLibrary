package com.pancarte.microservice.repository;


import com.pancarte.microservice.model.Book;
import com.pancarte.microservice.model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("borrowRepository")
public interface BorrowRepository extends JpaRepository<Borrow, Long> {



    @Query(value = "SELECT * FROM borrow u WHERE u.id_borrow > 0",nativeQuery = true)
    List<Borrow> findAllBorrowBook();



}
