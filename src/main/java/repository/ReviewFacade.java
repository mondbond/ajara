package repository;

import entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ReviewFacade extends AbstractFacade<Review> {
    final Logger LOGGER = LoggerFactory.getLogger(ReviewFacade.class);

    ReviewFacade() {
        super(Review.class);
    }

    /**
     * Get paginating list of entities for params
     *
     * @param skip       count of rows to skip
     * @param limit      count of rows to select
     * @param sortColumn sorting column
     * @param isAsc      is sorting order ASC
     * @param bookId     id of a book
     * @return list of entities
     */
    public List<Review> getPagination(int skip, int limit, String sortColumn, boolean isAsc, Long bookId) {
        LOGGER.info("IN getPagination:(of = [{}], skip = [{}], limit = [{}], sort column = [{}], is ASC = [{}])",
                Review.class.getSimpleName(), skip, limit, sortColumn, isAsc);
        String sqlString = "from Review R where book.id = " + bookId + " order by " + sortColumn + " " +
                ((isAsc) ? "ASC" : "DESC");
        Query query = getEntityManager().createQuery(sqlString, Review.class);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        List<Review> result = query.getResultList();
        LOGGER.debug("OUT getPagination:returned list of [{}], size = [{}]", Review.class.getSimpleName(),
                result.size());
        return result;
    }

    /**
     * Count all rows. It's needed for pagination logic
     *
     * @return count of results
     */
    public int countAllByBookPk(Long pk) {
        LOGGER.info("IN countAllByBookPk:(book pk = [{}])", pk);
        int result = ((Long) getEntityManager().createQuery("SELECT count(*) from Review r where r.book = " +
                pk).getSingleResult()).intValue();
        return result;
    }
}
