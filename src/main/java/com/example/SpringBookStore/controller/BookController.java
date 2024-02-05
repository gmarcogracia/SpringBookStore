package com.example.SpringBookStore.controller;


import com.example.SpringBookStore.entity.Book;
import com.example.SpringBookStore.entity.LoanedBook;
import com.example.SpringBookStore.repository.BookRepository;
import com.example.SpringBookStore.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class BookController {
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    BookRepository bookRepository;
    @GetMapping({"/form","/crud/books/create"})
    public String bookCrudForm(Model model){


        model.addAttribute("book",new Book());
        return "form";

    }

    //TODO AÑADIR EDIT Y DELETE Y UPDATE A LIBROS Y COPIES
    @GetMapping({"/","/booklist"})
    public String showBookList(Model model){
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "booklist";
    }
    @PostMapping("/crud/books/create/submit")
    public RedirectView saveBook(@ModelAttribute Book book, @RequestParam("image") MultipartFile multipartFile) throws IOException{
      /*  LoanedBook lb =
        book.setAvailableCopies(); Habria que  hacer un count de la otra tabla con los que tengan el mismo book_id TODO Implementarlo cuando tenga el LoanedRepository*/



        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "myImages/";

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        book.setImages(fileName);
        bookRepository.save(book);
        return new RedirectView("/crud/books/create", true);


        //"redirect:/booklist";
        //"redirect:/crud/books/create";
    }

    @PostMapping("/crud/books/modify/{id}")
    public String modifyBook(@PathVariable long id, Model model) {
        Book book=bookRepository.findById(id);
        model.addAttribute("book", book);
        return "formulario";
    }
    @PostMapping("/crud/books/modify/submit")
    public String modificarEntrada(@ModelAttribute("book") Book book){
        bookRepository.save(book);
        return "redirect:/crud/books";
}}
