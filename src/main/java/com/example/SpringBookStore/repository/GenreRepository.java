package com.example.SpringBookStore.repository;

import com.example.SpringBookStore.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository

public interface GenreRepository extends JpaRepository<Genre, Long> {
   public Genre findById(long id);
   public Genre findByName(String name);
   public ArrayList<Genre> findAll();




}
