package managers;

import entity.Review;
import exception.ReviewException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ReviewFacade;
import repository.ReviewHome;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ReviewManager {
    final Logger LOGGER = LoggerFactory.getLogger(ReviewManager.class);

    @EJB
    private ReviewHome reviewHome;

    @EJB
    private ReviewFacade reviewFacade;

    public void createReview(Review review) throws ReviewException {
        try {
            reviewHome.insert(review);
        }catch (Exception e){
            throw new ReviewException("Something happen while you trying to save review", e);
        }
    }

    /**
     * Get paginating list of entities for params
     * @param skip count of rows to skip
     * @param limit count of rows to select
     * @param sortColumn sorting column
     * @param isAsc is sorting order ASC
     * @param bookId id of a book
     * @return list of entities
     * */
    public List<Review> getPagination(int skip, int limit, String sortColumn, boolean isAsc, Long bookId) {
        LOGGER.info("IN getPagination:(of = [{}], skip = [{}], limit = [{}], sort column = [{}], is ASC = [{}])",
                Review.class.getSimpleName(), skip, limit, sortColumn, isAsc);
        String sqlString = "from Review R where book.id = " + bookId + " order by " + sortColumn + " " + ((isAsc)? "ASC": "DESC");
        Query query = reviewFacade.getEntityManager().createQuery(sqlString, Review.class);
        query.setFirstResult(skip);
        query.setMaxResults(limit);
        List<Review> result = query.getResultList();
        LOGGER.debug("OUT getPagination:returned list of [{}], size = [{}]", Review.class.getSimpleName(), result.size());
        return result;
    }

    public void deleteReview(Long pk) throws ReviewException {
        try {
            reviewHome.deleteByPk(pk);
        }catch (Exception e){
            throw new ReviewException("Something happen while you trying to delete review", e);
        }
    }
}
