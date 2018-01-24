package managers;

import entity.Reviews;
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

    public void createReview(Reviews review){
        reviewHome.insert(review);
    }
}
