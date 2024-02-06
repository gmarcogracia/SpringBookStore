package com.example.SpringBookStore.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="books")

public class Book {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private  String title;

    @Column(nullable=false)
    private  String isbn;


    @Column
    private int availableCopies;

    @Column
    private double price;

    @Column
    private  String tags;
    @Column
    private String author;

    @ManyToOne
    private Genre genre;

    //TODO HACER ISBN UNIQUE Y ARREGLAR BASE DE DATOS PARA TENER SOLO ENTRADAS BUENAS
    @Column
    private  String images;




    /**
    @ManyToOne
    private Genre genre;
     Fucks up the listing and overcomplicates things TODO Implement it properly if there's time SOLVED! :D
**/



    @Transient
    public String getPhotosImagePath() {
        if (images == null || id == null) return null;

        return "/uploads/" + id + "/" + images;
    }

}
