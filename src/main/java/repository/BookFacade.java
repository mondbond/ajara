package repository;

import entity.Author;
import entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class BookFacade extends AbstractFacade<Book> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookFacade.class);

    public BookFacade() {
        super(Book.class);
    }

    /**
     * Get paginating list of books by rating
     *
     * @param from       start range
     * @param to         end range
     * @param skip       count of rows to skip
     * @param limit      count of rows to select
     * @param sortColumn sorting column
     * @param isAsc      is sorting order ASC
     * @return list of Book
     */
    public List<Book> getPaginationByRating(int from, int to, int skip, int limit, String sortColumn, boolean isAsc) {
        LOGGER.info("getPaginationByRating:(from = [{}], to = [{}], skip = [{}], limit = [{}], sort column = [{}]," +
                " is ASC = [{}])", from, to, skip, limit, sortColumn, isAsc);
        String sqlString = "from " + Book.class.getName() + " b where b.averageRating > " +
                from + " and b.averageRating <= " + to + " order by " + sortColumn + " " + ((isAsc) ? "ASC" : "DESC");
        Query query = getEntityManager().createQuery(sqlString, Book.class);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        List<Book> result = query.getResultList();
        LOGGER.debug("OUT getPaginationByRating:returned list of [{}], size = [{}]", Book.class.getSimpleName(),
                result.size());
        return result;
    }

    /**
     * Get paginating list of books by rating with range
     *
     * @param from smaller parameter
     * @param to   bigger parameter
     * @return list of Book
     */
    public Long getCountByRatingRange(int from, int to) {
        LOGGER.info("IN getCountByRatingRange:(from = [{}]), to = [{}]", from, to);
        Query query = getEntityManager().createNamedQuery(Book.QUERY_COUNT_BY_RATING);
        query.setParameter(1, (float) from);
        query.setParameter(2, (float) to);
        Long result = (Long) query.getSingleResult();
        LOGGER.debug("OUT getCountByRatingRange:(entity = [{}]), count = [{}]", Book.class, result);
        return result;
    }

    /**
     * Get paginating list of books by Author
     *
     * @param author     author of books
     * @param skip       count of rows to skip
     * @param limit      count of rows to select
     * @param sortColumn sorting column
     * @param isAsc      is sorting order ASC
     * @return list of Book
     */
    public List<Book> getPaginationByAuthor(Author author, int skip, int limit, String sortColumn, boolean isAsc) {
        LOGGER.info("IN getPaginationByAuthor:(author = [{}], skip = [{}], limit = [{}], sort column = [{}], " +
                "is ASC = [{}])", author, skip, limit, sortColumn, isAsc);
        String sqlString = "select * from Book b inner join JOIN_BOOK_AUTHOR j on j.author_id = " + author.getId() +
                " and j.book_id = b.id order by " + sortColumn + " " + ((isAsc) ? "ASC" : "DESC");
        Query query = getEntityManager().createNativeQuery(sqlString, Book.class);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        List<Book> result = query.getResultList();
        LOGGER.debug("OUT getPaginationByAuthor:returned list of [{}], size = [{}]", Book.class.getSimpleName(),
                result.size());
        return result;
    }

    /**
     * Get paginating list of books by Author
     *
     * @param author author of books
     * @return list of Book
     */
    public Long getCountByAuthor(Author author) {
        LOGGER.info("IN getCountByAuthor:(author = [{}])", author);
        String sqlString = "SELECT COUNT(*) from Book b inner join JOIN_BOOK_AUTHOR j on j.author_id = " +
                author.getId() + " and j.book_id = b.id";
        Query query = getEntityManager().createNativeQuery(sqlString);
        Long result = ((BigDecimal) query.getSingleResult()).longValue();
        LOGGER.debug("OUT getCountByAuthor:returned list of [{}], size = [{}]", Book.class.getSimpleName(), result);
        return result;
    }
}
