package managers;
import entity.Book;
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

    public Book getBookByPk(long pk) {
        return bookFacade.findByPk(pk);
    }

    public List<Book> getAllBooks() {
        return bookFacade.findAll();
    }

    public void save(Book book) {
        bookHome.insert(book);
    }

    public void update(Book book) {
        bookHome.update(book);
    }

    public void delete(long pk){
        bookHome.deleteByPk(pk);
    }

    public void deleteList(List<Long> ids){
        bookHome.deleteList(ids);
    }

    public List<Book> filterByRating(int rating) {
        return bookFacade.filterByRatingRange(rating - 1, rating);
    }

    public Long getCountByRating(int from, int to){
        return bookFacade.getCountByRatingRange(from, to);
    }
}
