package managers;

import entity.Reviews;
import exception.ReviewException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ReviewHome;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ReviewManager {
    final Logger logger = LoggerFactory.getLogger(ReviewManager.class);

    @EJB
    private ReviewHome reviewHome;

    public void createReview(Reviews review) throws ReviewException {
        try {
            reviewHome.insert(review);
        }catch (Exception e){
            throw new ReviewException("Something happen while you trying to save review", e);
        }
    }

    public void deleteReview(Long pk) throws ReviewException {
        try {
            reviewHome.deleteByPk(pk);
        }catch (Exception e){
            throw new ReviewException("Something happen while you trying to delete review", e);
        }
    }
}
