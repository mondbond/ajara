package controllers;

import entity.Book;
import entity.Reviews;
import exception.BookException;
import exception.ReviewException;
import lombok.Getter;
import lombok.Setter;
import managers.BookManager;
import managers.ReviewManager;
import model.ReviewDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "reviewController")
@SessionScoped
public class ReviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    private @Setter @Getter Reviews newReview = new Reviews();

    @EJB
    private ReviewManager reviewManager;

    @EJB
    private BookManager bookManager;

    @EJB
    private @Setter ReviewDataModel dataModel;

    /**
     * Create review entity with pointed book
     * @param book book of review relate to
     * */
    public void createReview(Book book) throws BookException, ReviewException {
        LOGGER.info("IN createReview(book = [{}])", book);
        newReview.setBook(bookManager.getBookByPk(book.getId()));
        reviewManager.createReview(newReview);
        newReview = new Reviews();
    }

    /**
     * Delete review by pk
     * @param pk pk of review
     * */
    public void deleteReview(Long pk) throws ReviewException {
        reviewManager.deleteReview(pk);
    }

    /**
     * Get reviews by book
     * @param id book id of what reviews are you need
     * @return ReviewDataModel with seted book id in
     * */
    public ReviewDataModel getReviewDataModelByBook(Long id) {
        dataModel.setBookId(id);
        return dataModel;
    }
}
