package controllers;

import data.entity.Author;
import data.entity.Book;
import lombok.Getter;
import lombok.Setter;
import managers.BookManager;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Date;

@ManagedBean
@SessionScoped
public class BookController {

    @EJB
    BookManager bookManager;

    private @Setter @Getter Book newBook = new Book();

    public BookController() { }

    public Book getByPk(long pk){
        Book book = bookManager.getBookByPk(1);
        return book;
    }

    public void createBook(Author author){
        ArrayList<Author> authors = (ArrayList<Author>) newBook.getAuthors();
        authors.add(author);
        newBook.setAuthors(authors);
        newBook.setCrateDate(new Date());
        bookManager.save(newBook);
        newBook = new Book();
    }
}
