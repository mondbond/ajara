package repository;

import entity.Author;
import entity.Book;
import model.BookJPAModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BookFacade extends AbstractFacade<Book> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookFacade.class);

    private BookJPAModel bookModel;

    public BookFacade() {
        super(Book.class);
    }

    /**
     * Return list of books with rating in range
     * @param from int for start range
     * @param to int for end of range
     * @return list of books
     * */
    public List<Book> filterByRatingRange(Integer from, Integer to){
        LOGGER.info("IN filterByRatingRange:(from = [{}]), to = [{}]", from, to);

        Query query = getEntityManager().createNamedQuery(Book.QUERY_BY_RATING, Book.class);
        query.setParameter(1, from);
        query.setParameter(2, to);
        List<Book> result = query.getResultList();
        LOGGER.debug("OUT filterByRatingRange:(entity = [{}]), size = [{}]", Book.class, result.size());
        return result;
    }

    /**
     * Calculate count of books with rating in range
     * @param from int for start range
     * @param to int for end of range
     * @return count of books
     * */
    public Long getCountByRatingRange(int from, int to){
        LOGGER.info("IN getCountByRatingRange:(from = [{}]), to = [{}]", from, to);

        Query query = getEntityManager().createNamedQuery(Book.QUERY_COUNT_BY_RATING);
        query.setParameter(1,  new Float(from));
        query.setParameter(2, new Float(to));
        Long result = (Long) query.getSingleResult();
        LOGGER.debug("OUT getCountByRatingRange:(entity = [{}]), count = [{}]", Book.class, result);
        return result;
    }

    /**
     * Get paginating list of books by Author
     * @param author author of books
     * @param skip count of rows to skip
     * @param limit count of rows to select
     * @param sortColumn sorting column
     * @param isAsc is sorting order ASC
     * @return list of Book
     * */
    public List<Book> getPaginationByAuthor(Author author, int skip, int limit, String sortColumn, boolean isAsc) {
        LOGGER.info("IN getPaginationByAuthor:(author = [{}], skip = [{}], limit = [{}], sort column = [{}], is ASC = [{}])",author, skip, limit, sortColumn, isAsc);

        String sqlString = "SELECT * FROM BOOK b WHERE b.ID IN (SELECT BOOK_ID FROM JOIN_BOOK_AUTHOR WHERE AUTHOR_ID = " + author.getId() + ") ORDER BY " + sortColumn + " " + ((isAsc)? "ASC": "DESC");
//        String sqlStringjOIN = "from " + Book.class.getName() + " b inner join JOIN_BOOK_AUTHOR j on j.author_id = "+ author.getId()+ " and j.book_id = b.id order by " + sortColumn + " " + ((isAsc)? "ASC": "DESC");
        Query query = getEntityManager().createNativeQuery(sqlString, Book.class);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        List<Book> result = query.getResultList();
        LOGGER.debug("OUT getPaginationByAuthor:returned list of [{}], size = [{}]", Book.class.getSimpleName(), result.size());
        return result;
    }

    /**
     * Get paginating list of books by rating
     * @param from start range
     * @param to end range
     * @param skip count of rows to skip
     * @param limit count of rows to select
     * @param sortColumn sorting column
     * @param isAsc is sorting order ASC
     * @return list of Book
     * */
    public List<Book> getPaginationByRating(int from , int to,  int skip, int limit, String sortColumn, boolean isAsc) {
        LOGGER.info("getPaginationByRating:(from = [{}], to = [{}], skip = [{}], limit = [{}], sort column = [{}], is ASC = [{}])",from, to, skip, limit, sortColumn, isAsc);

        String sqlString = "from " + Book.class.getName() + " b where b.avgRating > " + from +" and b.avgRating <= " + to + " order by " + sortColumn + " " + ((isAsc)? "ASC": "DESC");
        Query query = getEntityManager().createQuery(sqlString, Book.class);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        List<Book> result = query.getResultList();
        LOGGER.debug("OUT getPaginationByRating:returned list of [{}], size = [{}]", Book.class.getSimpleName(), result.size());
        return result;
    }

    public BookJPAModel getModel() {
        if(bookModel == null) {
            bookModel = new BookJPAModel(getEntityManager(), Author.DATE_COLUMN, Book.class);
        }
        return bookModel;
    }
}
