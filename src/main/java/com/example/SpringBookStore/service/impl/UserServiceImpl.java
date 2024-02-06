package com.example.SpringBookStore.service.impl;

import com.example.SpringBookStore.dto.UserDto;
import com.example.SpringBookStore.entity.Role;
import com.example.SpringBookStore.entity.User;
import com.example.SpringBookStore.repository.RoleRepository;
import com.example.SpringBookStore.repository.UserRepository;
import com.example.SpringBookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());


       //TODO FIX MIXMATCH BETWEEN ENCRYPTED PASSWORDS AND RAW PASSWORDS  ASK ANTONIO  user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("CLIENT");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setPenalty(user.getPenalty());
        userDto.setLoanedBook(user.getLoanedBook());


        return userDto;
    }


    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    public boolean isLoggedIn() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {//Apparently this checks if theres an autenticated user TESTED, CONDITIONAL WORKS
            return true;
        } else {
            return false;
        }
    }

    public boolean isAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {//Apparently this checks if theres an autenticated user TESTED, CONDITIONAL WORKS


            User user = this.userRepository.findByEmail(authentication.getName());
            Role admin = this.roleRepository.findByName("ROLE_ADMIN");
            if (user.getRoles().contains(admin)) {
                return true;
            }
            return false;
        }
        return false;

    }

}
