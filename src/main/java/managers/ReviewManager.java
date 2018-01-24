package managers;

import entity.Book;
import entity.Reviews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;
import repository.ReviewHome;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ReviewManager {
    final Logger logger = LoggerFactory.getLogger(ReviewManager.class);

    @EJB
    ReviewHome reviewHome;

    @EJB
    BookFacade bookFacade;

    public void createReview(Reviews review){
        reviewHome.insert(review);
    }

    public Book getBookById(long pk){
        return bookFacade.findByPk(pk);
    }
}
