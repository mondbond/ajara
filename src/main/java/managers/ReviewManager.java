package managers;

import entity.Review;
import exception.ReviewException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ReviewFacade;
import repository.ReviewHome;

import javax.ejb.*;
import java.util.List;

import static javax.ejb.TransactionAttributeType.REQUIRED;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = REQUIRED)
public class ReviewManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewManager.class);

    @EJB
    private ReviewHome reviewHome;

    @EJB
    private ReviewFacade reviewFacade;

    public void createReview(Review review) throws ReviewException {
        try {
            reviewHome.insert(review);
        } catch (Exception e) {
            throw new ReviewException("Enable to save review", e);
        }
    }

    public void deleteReview(Long pk) throws ReviewException {
        try {
            reviewHome.deleteByPk(pk);
        } catch (Exception e) {
            throw new ReviewException("Enable to delete review", e);
        }
    }

    public List<Review> getPagination(int skip, int limit, String sortColumn, boolean isAsc, Long bookId) throws ReviewException {
        try {
            return reviewFacade.getPagination(skip, limit, sortColumn, isAsc, bookId);
        } catch (Exception e) {
            throw new ReviewException("Enable to get authors autocomplete", e);
        }
    }

    public int countAllByBookId(Long bookPk) throws ReviewException {
        try {
            return reviewFacade.countAllByBookPk(bookPk);
        } catch (Exception e) {
            throw new ReviewException("Enable to count books by rating", e);
        }
    }
}
