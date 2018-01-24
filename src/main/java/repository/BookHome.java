package repository;

import entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class BookHome extends AbstractHome<Book> {
    final Logger logger = LoggerFactory.getLogger(BookHome.class);

    public BookHome() {
        super(Book.class);
    }
}
