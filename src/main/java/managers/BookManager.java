package managers;

import entity.Author;
import entity.Book;
import util.exception.BookException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;
import repository.BookHome;

import javax.ejb.*;
import java.util.List;

import static javax.ejb.TransactionAttributeType.REQUIRED;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = REQUIRED)
public class BookManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookManager.class);

    @EJB
    private BookFacade bookFacade;

    @EJB
    private BookHome bookHome;

    public Book getBookByPk(long pk) throws BookException {
        try {
            return bookFacade.findByPk(pk);
        } catch (Exception e) {
            throw new BookException("Enable to find book", e);
        }
    }

    public void save(Book book) throws BookException {
        try {
            bookHome.insert(book);
        } catch (Exception e) {
            throw new BookException("Enable to save book", e);
        }
    }

    public void update(Book book) throws BookException {
        try {
            bookHome.update(book);
        } catch (Exception e) {
            throw new BookException("Enable to update book", e);
        }
    }

    public void delete(long pk) throws BookException {
        try {
            bookHome.deleteByPk(pk);
        } catch (Exception e) {
            throw new BookException("Enable to delete book", e);
        }
    }

    public void deleteList(List<Long> pkList) throws BookException {
        try {
            bookHome.deleteByPkList(pkList);
        } catch (Exception e) {
            throw new BookException("Enable to delete books", e);
        }
    }

    public boolean isUnique(String columnName, String value) throws BookException {
        try {
            return bookFacade.isUnique(columnName, value);
        } catch (Exception e) {
            throw new BookException("Error occured while you trying understand is this value is unique", e);
        }
    }

    public List<Book> getPagination(int skip, int limit, String sortColumn, boolean isAsc) throws BookException {
        try {
            return bookFacade.getPagination(skip, limit, sortColumn, isAsc);
        } catch (Exception e) {
            throw new BookException("Enable to get authors autocomplete", e);
        }
    }

    public int countAll() throws BookException {
        try {
            return bookFacade.countAll();
        } catch (Exception e) {
            throw new BookException("Enable to get authors autocomplete", e);
        }
    }

    public List<Book> getPaginationByRating(int from, int to, int skip, int limit, String sortColumn, boolean isAsc)
            throws BookException {
        try {
            return bookFacade.getPaginationByRating(from, to, skip, limit, sortColumn, isAsc);
        } catch (Exception e) {
            throw new BookException("Enable to count books by rating", e);
        }
    }

    public Long getCountByRatingRange(int from, int to) throws BookException {
        try {
            return bookFacade.getCountByRatingRange(from, to);
        } catch (Exception e) {
            throw new BookException("Enable to count books by rating", e);
        }
    }

    public List<Book> getPaginationByAuthor(Author author, int skip, int limit, String sortColumn, boolean isAsc)
            throws BookException {
        try {
            return bookFacade.getPaginationByAuthor(author, skip, limit, sortColumn, isAsc);
        } catch (Exception e) {
            throw new BookException("Enable to count books by rating", e);
        }
    }

    public Long getCountByAuthor(Author author) throws BookException {
        try {
            return bookFacade.getCountByAuthor(author);
        } catch (Exception e) {
            throw new BookException("Enable to count books by rating", e);
        }
    }
}
