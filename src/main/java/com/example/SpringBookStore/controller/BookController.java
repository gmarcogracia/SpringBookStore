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
    public String showBookList(Model model){
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
            model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        return  "booklist";
    }
    @PostMapping("/crud/books/create/submit")
    public RedirectView saveBook(@ModelAttribute Book book, @RequestParam("image") MultipartFile multipartFile) throws IOException{
      /*  LoanedBook lb =
*/
        //Asumimos que solo vamos a añadir un libro a nuestra base de datos cuando obtengamos un ejemplar En caso de que no fuese asi ,comentar las siguientes 4 lineas
        book.setAvailableCopies(1);
        LoanedBook copy = new LoanedBook();
        copy.setBook(book);
        loanedbookRepository.save(copy);



        Date date= new Date();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()) + date.toString();
        String uploadDir = "myImages/";

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        book.setImages(fileName);
        bookRepository.save(book);
        return  new RedirectView("/crud/books/create", true);


        //"redirect:/booklist";
        //"redirect:/crud/books/create";
    }

    @GetMapping("/crud/books/modify/{id}")
    public String modifyBook(@PathVariable long id, Model model) {
        Book book=bookRepository.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        return  "form";
    }
    @PostMapping("/crud/books/modify/submit")
    public String modificarEntrada(@ModelAttribute("book") Book book){
        bookRepository.save(book);

        return  "redirect:/";
}
    @DeleteMapping("/crud/books/delete/{id}")
    public String deleteBook(@PathVariable long id){
        bookRepository.deleteById(id);
        return "redirect:/";
    }
}
