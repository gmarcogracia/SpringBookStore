package com.example.SpringBookStore.repository;

import com.example.SpringBookStore.entity.Book;
import com.example.SpringBookStore.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    public Book findById(long id);
    public  ArrayList<Book> findAll();
    public ArrayList<Book> findByGenre(Genre genre);//TODO IMPLEMENT FILTERS AFTER EEVERYTHING WORKS
    public void deleteById(long id);
    public Book save(Book book);
    public ArrayList<Book> findByTags(String tags);
    public ArrayList<Book> findByAuthor(String author);
    public ArrayList<Book> findByTitleContaining(String title);
    public ArrayList<Book> findByIsbn(String isbn);



}
