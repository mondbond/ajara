package managers;

import entity.Book;
import exception.BookException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;
import repository.BookHome;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class BookManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookManager.class);

    @EJB
    private BookFacade bookFacade;

    @EJB
    private BookHome bookHome;

    public Book getBookByPk(long pk) throws BookException {
        try {
            return bookFacade.findByPk(pk);
        }catch (Exception e){
            throw new BookException("Something happen while you trying to find book", e);
        }
    }

    public void save(Book book) throws BookException {
        try {
            bookHome.insert(book);
        }catch (Exception e){
            throw new BookException("Something happen while you trying to save book", e);
        }
    }

    public void update(Book book) throws BookException {
        try {
            bookHome.update(book);
        }catch (Exception e){
            throw new BookException("Something happen while you trying to update book", e);
        }
    }

    public void delete(long pk) throws BookException {
        try {
            bookHome.deleteByPk(pk);
        }catch (Exception e){
            throw new BookException("Something happen while you trying to delete book", e);
        }
    }

    public void deleteList(List<Long> ids) throws BookException {
        try {
            List<Book> books = bookFacade.findByPks(ids);
            books.forEach(book -> {
                bookHome.deleteByPk(book.getId());
            });
        }catch (Exception e){
            throw new BookException("Something happen while you trying to delete books", e);
        }
    }

    public boolean isUnique(String columnName, String value) throws BookException {
        try {
            return bookFacade.isUnique(columnName, value);
        }catch (Exception e){
            throw new BookException("Something happen while you trying understand is this value is unique", e);
        }
    }

    public Long getCountByRatingRange(int from, int to) throws BookException {
        try {
            return bookFacade.getCountByRatingRange(from, to);
        }catch (Exception e){
            throw new BookException("Something happen while you trying to count books by rating", e);
        }
    }
}
