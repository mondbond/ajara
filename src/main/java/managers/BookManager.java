package managers;
import data.entity.Book;
import lombok.Getter;
import model.BookDataModule;
import repository.BookFacade;
import repository.BookHome;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class BookManager {

    @EJB
    private BookFacade bookFacade;

    @EJB
    private BookHome bookHome;

    @EJB
    private @Getter BookDataModule dataModule;

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
        Stream.of(ids).flatMap(pk->ids.stream()).forEach(pk->delete(pk));
    }
}
