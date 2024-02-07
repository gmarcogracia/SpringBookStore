package com.example.SpringBookStore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;





@Configuration
@EnableWebSecurity
public class SpringSecurity {
//TODO ADD FILTER BY AUTHOR, NAME AND MAYBE GENRE IF THERE'S TIME
    @Autowired
    private UserDetailsService userDetailsService;
    private  AuthorizeHttpRequestsConfigurer authorizeHttpRequestsConfigurer;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
/** Posssible solution, create custom HasRole Method using userService isAdmin() method
    public AuthorizeHttpRequestsConfigurer(String role) {
        return AuthorizeHttpRequestsConfigurer.access(AuthorityAuthorizationManager.hasRole(role));
    }**/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("**/myImages/**").permitAll()
                                .requestMatchers("/users").authenticated()//hasRole("ROLE_ADMIN") TODO FIX THIS CUTRE FIX: REDIRECT IN CONTROLLER
                                .requestMatchers("/crud/**","/crud/books/**","/crud/books/modify/submit/crud/books/modify/submit").authenticated()   //hasRole("ROLE_ADMIN")
                                .requestMatchers("/books/**","/loans/**").permitAll()
                                .requestMatchers("/myAccount").authenticated()
                                .requestMatchers("/form").authenticated()
                                .requestMatchers("/loans/getLoan/**","/loans/return/**").authenticated()
                                .requestMatchers("/booklist/**","/").permitAll()

                                .requestMatchers("/?continue").authenticated()

                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();

            /*formLogin(
                            form -> form
                                    .loginPage("/booklist")//This redirects you to /booklist after entering the page
                                    .loginProcessingUrl("/login")
                                    .defaultSuccessUrl("/users")
                                    .permitAll()
                                    ).*/
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}

