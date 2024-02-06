package com.example.SpringBookStore.controller;

import com.example.SpringBookStore.entity.Book;
import com.example.SpringBookStore.entity.LoanedBook;
import com.example.SpringBookStore.entity.User;
import com.example.SpringBookStore.repository.BookRepository;
import com.example.SpringBookStore.repository.LoanedBookRepository;
import com.example.SpringBookStore.repository.RoleRepository;
import com.example.SpringBookStore.repository.UserRepository;
import com.example.SpringBookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Controller
public class LoanedBookController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    LoanedBookRepository loanedBookRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @GetMapping("/loans/{id}")
    public String showCopies(@PathVariable long id, Model model) {


        model.addAttribute("copies", loanedBookRepository.findByBookId(id));
        model.addAttribute("bookId",id);//COuld be unnecesary, added before book too lazy to change it rn
        model.addAttribute("book",bookRepository.findById(id));
        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        return "listCopies";

    }


    @GetMapping("/crud/copies/{id}/create")

    public String addCopyForm(@PathVariable long id,Model model) {
        LoanedBook copy = new LoanedBook();
        Book og = bookRepository.findById(id);
        copy.setBook(og);
        loanedBookRepository.save(copy);
        og.setAvailableCopies(loanedBookRepository.countByBookId(id));
        bookRepository.save(og);

        return "redirect:/loans/"+id;

    }
    @GetMapping("/loans/getloan/{id}")
    public String modifyBook(@PathVariable long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        if (!(authentication instanceof AnonymousAuthenticationToken)) {//Apparently this checks if theres an autenticated user TESTED, CONDITIONAL WORKS


            User user = userRepository.findByEmail(authentication.getName());
            if (user.getLoanedBook()!=null){
                return "loanError";
            }
            LoanedBook copy=loanedBookRepository.findById(id);
        copy.setUser(user);

            System.out.println(authentication.getName());
            System.out.println(authentication.getClass());
            System.out.println(authentication.getCredentials());
        Date now = new Date();
        copy.setLoanDate(now);
            Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH,20);
        copy.setLoanExpiryDate(calendar.getTime());
        //This should add 20 days to original date, TESTED,works
        loanedBookRepository.save(copy);
            user.setLoanedBook(copy);
            userRepository.save(user);


        return "redirect:/bookList";
    }else{
            return "redirect:/login";
        }

}

    @GetMapping("/loans/return/{id}")
    public String returnBook(@PathVariable long id, Model model){
        LoanedBook copy =loanedBookRepository.findById(id);
        User user = copy.getUser();
        copy.setLoanExpiryDate(null);
        copy.setLoanDate(null);
        copy.setUser(null);
        user.setLoanedBook(null);
        userRepository.save(user);
        loanedBookRepository.save(copy);
        return "redirect:/myAccount";
//Podria añadirsele otra tabla que guardase los registros de todos los prestamos hechos con anterioridad TODO implementarlo si hay tiempo (Probablemente no)

    }

}