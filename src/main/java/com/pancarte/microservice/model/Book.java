package com.pancarte.microservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="book")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_role")
    private int idRole;

    @Column(name = "id_type")
    private int idType;

    @Column(name = "id_editeur")
    private int idEditeur;

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Column(name = "url_image")
    private String urlImage;

    @Column(name = "isbn")
    private String isbn;

    @DateTimeFormat(pattern = "yyyy")
    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "price")
    private int price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "creation_date")
    private Date creationDate;


 @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "update_date")
    private Date updateDate;



}
