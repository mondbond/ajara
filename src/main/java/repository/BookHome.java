package repository;

import entity.Book;

import javax.ejb.Stateless;

@Stateless
public class BookHome extends AbstractHome<Book> {

    public BookHome() {
        super(Book.class);
    }
}
