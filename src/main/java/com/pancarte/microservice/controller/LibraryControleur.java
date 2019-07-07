package com.pancarte.microservice.controller;

import com.pancarte.microservice.model.*;

import com.pancarte.microservice.repository.BorrowRepository;
import com.pancarte.microservice.repository.RoleRepository;
import com.pancarte.microservice.repository.UserRepository;
import com.pancarte.microservice.service.BookService;
import com.pancarte.microservice.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;

import java.time.LocalDate;
import java.time.ZoneId;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


import java.util.List;


@RestController
public class LibraryControleur {
    private final UserService userService;
    private final UserRepository userRepository;
    private final BookService bookService;
    private final BorrowRepository borrowRepository;

    private final RoleRepository roleRepository;






    @Autowired
    public LibraryControleur(UserService userService, BookService bookService, @Qualifier("borrowRepository") BorrowRepository borrowRepository, @Qualifier("roleRepository") RoleRepository roleRepository, @Qualifier("userRepository") UserRepository userRepository) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.bookService = bookService;
        this.borrowRepository = borrowRepository;
        this.roleRepository = roleRepository;
    }


    @RequestMapping(value = {"/user"}, method = RequestMethod.GET)
    public String queryUser(@RequestParam("email") String email) {
        String x = userRepository.queryUser(email);
        return x;
    }

    @Autowired
    private JavaMailSender javaMailSender;


    @Scheduled(cron = "0/20 * * * * ?")
    public void sendEmail() throws IOException {
      List<Borrow>  borrowed = borrowRepository.findAllBorrowBook();
        for (Borrow borrowedBook : borrowed){
            if((borrowedBook.getReturnDate()).compareTo(java.sql.Date.valueOf( LocalDate.now()))>0){
                User user = userRepository.findById(borrowedBook.getIdUser());
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(user.getEmail());
                msg.setSubject("Livre à rendre");
                msg.setText("vous deviez rendre le libre le :"+borrowedBook.getReturnDate()+"\\n Nous vous prions de retourner le livre ");
            }
        }

/*
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("marj12@live.fr");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");
        System.out.println(msg);

        javaMailSender.send(msg);
*/
    }




    @RequestMapping(value = {"/role"}, method = RequestMethod.GET)
    public String queryRole(@RequestParam("email") String email) {

        System.out.println(roleRepository.queryRole(email));
        return roleRepository.queryRole((email));
    }

    @RequestMapping(value = {"/find"}, method = RequestMethod.GET)
    public User findByMail(@RequestParam("email") String email) {
        System.out.println(userService.findUserByEmail(email));

        return userService.findUserByEmail(email);
    }

   
    @RequestMapping(value = {"/createUser"}, method = RequestMethod.GET)
    public void createUser(@RequestParam("lastName") String lastName, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("password") String password) {
        User user = new User();
        user.setLastName(lastName);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        System.out.println("TEST" + user.getLastName() + user.getName() + user.getEmail() + user.getPassword());
        userService.saveUser(user);
    }

    @RequestMapping(value = {"/getborrow"}, method = RequestMethod.GET)
    public List<Borrow> getborrowedBook(@RequestParam("idUser") int idUser) {
        return borrowRepository.findBorrowedBookByIUser(idUser);
    }

    @RequestMapping(value = {"/borrow"})
    public void borrow(@RequestParam("idBook") int idBook, @RequestParam("idUser") int idUser) {
        Date now = Date.valueOf(LocalDate.now());
        Borrow borrow = new Borrow();

        borrow.setIdBook(idBook);
        borrow.setIdUser(idUser);
        borrow.setLoan(true);
        borrow.setCreationDate(now);
        LocalDate today = LocalDate.now();
        System.out.println("Current date: " + today);
        //add 4 week to the current date
        LocalDate next4Week = today.plus(4, ChronoUnit.WEEKS);
        System.out.println("Next week: " + next4Week);
        System.out.println(java.sql.Date.valueOf(next4Week));
        borrow.setReturnDate(java.sql.Date.valueOf(next4Week));
        borrow.setExtended(false);
        borrowRepository.save(borrow);
    }

    @RequestMapping(value = {"/extendBorrow"}, method = RequestMethod.GET)
    public void extendBorrow(@RequestParam("idBorrow") int idBorrow) {
        Date now = Date.valueOf(LocalDate.now());
        Borrow borrowed = borrowRepository.findBorrowedBook(idBorrow);


        LocalDate creationDate = borrowed.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println("Current date: " + creationDate);
        LocalDate next4Week = creationDate.plus(4, ChronoUnit.WEEKS);
        System.out.println("Next week: " + next4Week);
        borrowed.setReturnDate(java.sql.Date.valueOf(next4Week));
        borrowed.setExtended(true);
        borrowRepository.save(borrowed);
    }

    @RequestMapping(value = {"/search"})
    public List<Book_List> searchIt(@RequestParam("search") String search) {

        String cap = search.substring(0, 1).toUpperCase() + search.substring(1);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equals("anonymousUser")) {
            System.out.println(auth.getName());
        } else {
            System.out.println(auth.getName() + "_________________");
        }
        List<Book> books = bookService.findByTitle(search);
        List<Book_List> listBook = new ArrayList<>();
        List<Borrow> borrowedBook = borrowRepository.findAllBorrowBook();

        int nbCopy = 0;
        int nbBorrow = 0;
        int contain = 0;
        for (Book bookList : books) {
            nbBorrow = nbCopy = 0;
            for (Book allbooks : books) {

                if (bookList.getTitle().equals(allbooks.getTitle())) {
                    nbCopy++;

                    for (Borrow bookBorrowed : borrowedBook) {
                        if ((allbooks.getIdBook() == bookBorrowed.getIdBook()) && (bookBorrowed.isLoan())) {
                            nbBorrow++;
                        }
                    }
                }
                if (books.indexOf(allbooks) == books.size() - 1) {
                    Book_List listedBook = new Book_List();
                    contain = 0;
                    listedBook.setIdBook(bookList.getIdBook());
                    listedBook.setIdEditeur(bookList.getIdEditeur());
                    listedBook.setIdType(bookList.getIdType());
                    listedBook.setTitle(bookList.getTitle());
                    listedBook.setSummary(bookList.getSummary());
                    listedBook.setUrlImage(bookList.getUrlImage());
                    listedBook.setIsbn(bookList.getIsbn());
                    listedBook.setPurchaseDate(bookList.getPurchaseDate());
                    listedBook.setPrice(bookList.getPrice());
                    listedBook.setUpdateDate(bookList.getUpdateDate());
                    listedBook.setCreationDate(bookList.getCreationDate());
                    listedBook.setNbCopy(nbCopy);
                    listedBook.setNbCopyAvailable(nbCopy - nbBorrow);
                    System.out.println(nbBorrow + " hhhhhh");
                    System.out.println(nbCopy + "nnnn");

                    for (Book_List boo : listBook) {
                        if (boo.getTitle().equals(listedBook.getTitle())) {
                            contain = 1;
                        }
                    }
                    if (contain == 0) {
                        listBook.add(listedBook);
                    }
                }
            }
        }

        return listBook;
    }



    @RequestMapping(value = {"/getAllBooks"})
    public List<Book_List> getAllBooks() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equals("anonymousUser")) {
            System.out.println(auth.getName());
        } else {
            System.out.println(auth.getName() + "_________________");
        }
        List<Book> books = bookService.findAll();
        List<Book_List> listBook = new ArrayList<>();
        List<Borrow> borrowedBook = borrowRepository.findAllBorrowBook();
        StringBuilder userb = new StringBuilder();
        int nbCopy = 0;
        int nbBorrow = 0;
        int contain = 0;
        for (Book bookList : books) {
            nbBorrow = nbCopy = 0;
            for (Book allbooks : books) {

                if (bookList.getTitle().equals(allbooks.getTitle())) {
                    nbCopy++;

                    for (Borrow bookBorrowed : borrowedBook) {
                        if ((allbooks.getIdBook() == bookBorrowed.getIdBook()) && (bookBorrowed.isLoan())) {
                            nbBorrow++;
                        }
                    }
                }
                if (books.indexOf(allbooks) == books.size() - 1) {
                    Book_List listedBook = new Book_List();
                    contain = 0;
                    listedBook.setIdBook(bookList.getIdBook());
                    listedBook.setIdEditeur(bookList.getIdEditeur());
                    listedBook.setIdType(bookList.getIdType());
                    listedBook.setTitle(bookList.getTitle());
                    listedBook.setSummary(bookList.getSummary());
                    listedBook.setUrlImage(bookList.getUrlImage());
                    listedBook.setIsbn(bookList.getIsbn());
                    listedBook.setPurchaseDate(bookList.getPurchaseDate());
                    listedBook.setPrice(bookList.getPrice());
                    listedBook.setUpdateDate(bookList.getUpdateDate());
                    listedBook.setCreationDate(bookList.getCreationDate());
                    listedBook.setNbCopy(nbCopy);
                    listedBook.setNbCopyAvailable(nbCopy - nbBorrow);
                    System.out.println(nbBorrow + " hhhhhh");
                    System.out.println(nbCopy + "nnnn");

                    for (Book_List boo : listBook) {
                        if (boo.getTitle().equals(listedBook.getTitle())) {
                            contain = 1;
                        }
                    }
                    if (contain == 0) {
                        listBook.add(listedBook);
                    }
                }
            }
        }

        return listBook;
    }


}
