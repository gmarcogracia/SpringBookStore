package com.example.SpringBookStore.controller;

import com.example.SpringBookStore.entity.Book;
import com.example.SpringBookStore.entity.LoanedBook;
import com.example.SpringBookStore.repository.BookRepository;
import com.example.SpringBookStore.repository.LoanedBookRepository;
import com.example.SpringBookStore.repository.UserRepository;
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


    @GetMapping("/loans/{id}")
    public String showCopies(@PathVariable long id, Model model) {


        model.addAttribute("copies", loanedBookRepository.findByBookId(id));
        model.addAttribute("bookId",id);//COuld be unnecesary, added before book too lazy to change it rn
        model.addAttribute("book",bookRepository.findById(id));
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
    @PostMapping("loans/getloan/{id}")
    public String modifyBook(@PathVariable long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {//Apparently this checks if theres an autenticated user UNTESTED
        LoanedBook copy=loanedBookRepository.findById(id);
        copy.setUser(userRepository.findByEmail(authentication.getName()));
        Date now = new Date();
        copy.setLoanDate(now);
            Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH,20);
        copy.setLoanExpiryDate(calendar.getTime());
        //This should add 20 days to original date, UNTESTED
        loanedBookRepository.save(copy);

        return "bookList";
    }else{
            return "redirect:/login";
        }

}}