package com.example.SpringBookStore.controller;

import com.example.SpringBookStore.dto.UserDto;
import com.example.SpringBookStore.entity.Book;
import com.example.SpringBookStore.entity.LoanedBook;
import com.example.SpringBookStore.entity.User;
import com.example.SpringBookStore.repository.BookRepository;
import com.example.SpringBookStore.repository.LoanedBookRepository;
import com.example.SpringBookStore.repository.UserRepository;
import com.example.SpringBookStore.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private LoanedBookRepository loanedBookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        user.setPenalty(0);
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/myAccount")
    public String showAccountDetails(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("isLogged",userService.isLoggedIn());
        model.addAttribute("isAdmin",userService.isAdmin());
        if (!(authentication instanceof AnonymousAuthenticationToken)) {//Redundant, should not even be accesible but whatever


            User user = userRepository.findByEmail(authentication.getName());
            model.addAttribute("user",user);
            LoanedBook copy=loanedBookRepository.findByUser(user);
            if(copy!=null){
            model.addAttribute("copy",copy);

            Book book = copy.getBook();
            model.addAttribute("book",book);
            }
            return "myAccount";

    }
        else{ return "bookList";}
}}
