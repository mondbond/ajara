package controllers;

import data.entity.Book;
import managers.BookManager;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class BookController {

    @EJB
    BookManager bookManager;

    public BookController() { }

    public Book getByPk(long pk){
        Book book = bookManager.getBookByPk(1);
        return book;

    }
}
