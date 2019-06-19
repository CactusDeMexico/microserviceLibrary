package com.pancarte.microservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "borrow")
@Getter
@Setter
public class Borrow {
    @Id
    @Column(name = "id_borrow")
    private int idBorrow;

    @Column(name = "id_book")
    private int idBook;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "isloan")
    private boolean isLoan;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "borrowing_date")
    private Date creationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "return_date")
    private Date returnDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "is_Extended")
    private  boolean isExtended;

}
