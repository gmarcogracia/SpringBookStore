package com.example.SpringBookStore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="loanedBooks")

public class LoanedBook {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;


 //These fields are temporary, this could be saved to another model to keep a registry of past loans
    @Column
    private Date loanDate;
    @Column
    private Date loanExpiryDate;


    @ManyToOne
    private  Book book;


//One user could have more than one book lent, but this is intented for a smaller library
    @OneToOne
    private  User user;







}
