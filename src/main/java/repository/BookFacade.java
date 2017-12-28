package repository;

import data.entity.Book;

import javax.ejb.Stateful;
import javax.ejb.Stateless;

@Stateless
public class BookFacade extends AbstractFacade<Book> {

    public BookFacade() {
        super(Book.class);
    }
}
