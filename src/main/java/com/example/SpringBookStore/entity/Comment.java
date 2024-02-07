package com.example.SpringBookStore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name="comments")
    public class Comment {

        private static final long serialVersionUID = 1L; //i HAVE NO IDEA WHAT THIS DOES but the original file had it so...
        @Id
        @GeneratedValue(strategy =  GenerationType.IDENTITY)
        private Long id;


        @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
        @JoinTable(
                name="users_comments",
                joinColumns={@JoinColumn(name="COMMENT_ID", referencedColumnName="ID")},
                inverseJoinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")})
        private List<User> users = new ArrayList<>();

        @ManyToOne
        private  Book book;

    }
