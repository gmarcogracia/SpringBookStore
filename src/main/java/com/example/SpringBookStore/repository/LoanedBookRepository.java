package com.example.SpringBookStore.repository;

import com.example.SpringBookStore.entity.LoanedBook;
import com.example.SpringBookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LoanedBookRepository extends JpaRepository<LoanedBook,Long> {
    public LoanedBook findById(long id);
    public ArrayList<LoanedBook> findAll();
    public ArrayList<LoanedBook> findByBookId(long id);
    public LoanedBook findByUserId(long id);
    public int countByBookId(long id); //Podria simplemente sacarse sacando el size del arrayList de bookId, De aqui saldria el campo available copies
    public void deleteById(long id);
    public  LoanedBook findByUser(User user);
    public LoanedBook save(LoanedBook book);

}
