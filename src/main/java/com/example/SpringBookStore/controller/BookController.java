package com.example.SpringBookStore.controller;


import com.example.SpringBookStore.entity.Book;
import com.example.SpringBookStore.entity.LoanedBook;
import com.example.SpringBookStore.repository.*;
import com.example.SpringBookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class BookController {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LoanedBookRepository loanedbookRepository;
    
    @Autowired
   private UserService userService;

    @GetMapping({"/form","/crud/books/create"})
    public String bookCrudForm(Model model){

        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        model.addAttribute("book",new Book());
         return  "form";

    }

    //TODO AÑADIR EDIT Y DELETE Y UPDATE A LIBROS Y COPIES
    @GetMapping({"/","/booklist"})
    public String showbooklist(Model model){
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
            model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        model.addAttribute("title",new String());
        return  "booklist";
    }

    //Filters
    @GetMapping("/booklist/filter/author/{author}")
    public String filterByAuthor(Model model ,@PathVariable String author){
        model.addAttribute("books", bookRepository.findByAuthor(author));
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        model.addAttribute("title",new String());
        return  "booklist";
    }

    @GetMapping("/booklist/filter/isbn/{isbn}")
    public String filterByIsbn(Model model ,@PathVariable String isbn){
        model.addAttribute("books", bookRepository.findByIsbn(isbn));
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        return  "booklist";
    }
    @GetMapping("/booklist/filter/title/{title}")
    public String filterByTitle(Model model ,@PathVariable String title){
        model.addAttribute("books", bookRepository.findByTitleContaining(title));
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        return  "booklist";
    }
    @PostMapping("/crud/books/create/submit")
    public RedirectView saveBook(@ModelAttribute Book book, @RequestParam("image") MultipartFile multipartFile) throws IOException{
      /*  LoanedBook lb =
*/

        if (userService.isAdmin()) {
            //Asumimos que solo vamos a añadir un libro a nuestra base de datos cuando obtengamos un ejemplar En caso de que no fuese asi ,comentar las siguientes 4 lineas
            book.setAvailableCopies(1);
            LoanedBook copy = new LoanedBook();



            Instant crrntdate = new Date().toInstant();
            String date = crrntdate.toString();

            date = date.replaceAll(":","");
            date = date.replaceAll("\\.","");
            date = date.replaceAll("-","");


            String fileName = date +StringUtils.cleanPath(multipartFile.getOriginalFilename()) ;
            String uploadDir = "myImages/";

            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            book.setImages(fileName);

            bookRepository.save(book);

            copy.setBook(book);

            loanedbookRepository.save(copy);
            return new RedirectView("/form", true);


            //"redirect:/booklist";
            //"redirect:/crud/books/create";
        } return new RedirectView("/login");
    }

    @GetMapping("/crud/books/modify/{id}" )
    public String modifyBook(@PathVariable long id, Model model) {
        if(userService.isAdmin()){
        Book book=bookRepository.findById(id);
        model.addAttribute("book", book);
            System.out.println(book);
        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        return  "form";}
        return "login";
    }


    @PostMapping("/crud/books/modify/submit")
    public String saveModifications(@ModelAttribute Book book, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        System.out.println(book.getId());
        bookRepository.save(book);


        Instant crrntdate = new Date().toInstant();
        String date = crrntdate.toString();
        date = date.replaceAll(":","");
        date = date.replaceAll("\\.","");
        date = date.replaceAll("-","");


        String fileName = date +StringUtils.cleanPath(multipartFile.getOriginalFilename()) ;
        String uploadDir = "myImages/";

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        book.setImages(fileName);
        //TODO FIX THIS (IT SHOULD WORK BUT I DON'T KNOW WHY IT DOES NOT)

        return  "redirect:/booklist";
}
/*
@PostMapping("/crud/books/modify/submit/crud/books/modify/submit")
    public String modificarEntrada2(@ModelAttribute Book book){
        bookRepository.save(book);

        return  "redirect:/booklist";
    }*/

    //TODO AÑADIR COMENTARIOS POSIBLEMENTE
    @GetMapping("/crud/books/delete/{id}")
    public String deleteBook(@PathVariable long id){
        if(userService.isAdmin()){
            ArrayList<LoanedBook> loanedBooks =loanedbookRepository.findByBookId(id);
            for (LoanedBook copy:loanedBooks) {
                loanedbookRepository.deleteById(copy.getId());

            }

        bookRepository.deleteById(id);
        return "redirect:/";}
        return  "login ";
    }
}
