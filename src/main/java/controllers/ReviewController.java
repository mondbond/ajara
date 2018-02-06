package controllers;

import entity.Book;
import entity.Reviews;
import exception.BookException;
import exception.ReviewException;
import lombok.Getter;
import lombok.Setter;
import managers.BookManager;
import managers.ReviewManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "reviewController")
@SessionScoped
public class ReviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    private @Setter @Getter Reviews newReview = new Reviews();

    @EJB
    private ReviewManager reviewManager;

    @EJB
    private BookManager bookManager;

    /**
     * Create review entity with pointed book
     * @param book Book of review relate to
     * */
    public void createReview(Book book) throws BookException, ReviewException {
        LOGGER.info("IN createReview(book = [{}])", book);
        newReview.setBook(bookManager.getBookByPk(book.getId()));
        reviewManager.createReview(newReview);
        newReview = new Reviews();
    }

    /**
     * Get reviews by book
     * @param book book of what reviews are you need
     * @return list of reviews
     * */
    public List<Reviews> getReviews(Book book) throws BookException {
        LOGGER.info("IN getReviews(book = [{}])", book);
        return bookManager.getBookByPk(book.getId()).getReviews();
    }

    public void deleteReview(Long pk) throws ReviewException {
        reviewManager.deleteReview(pk);
    }
}
