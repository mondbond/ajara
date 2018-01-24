package managers;

import entity.Book;
import entity.Reviews;
import repository.BookFacade;
import repository.ReviewFacade;
import repository.ReviewHome;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ReviewManager {

    @EJB
    ReviewFacade reviewFacade;

    @EJB
    ReviewHome reviewHome;

    @EJB
    BookFacade bookFacade;

    public void createReview(Reviews review){
        reviewHome.insert(review);
    }

    //TODO: getBookById shouldn't be there. Please move this to BookManager;
    public Book getBookById(long pk){
        return bookFacade.findByPk(pk);
    }
}
