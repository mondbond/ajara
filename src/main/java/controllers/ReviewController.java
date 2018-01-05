package controllers;

import data.entity.Book;
import data.entity.Reviews;
import lombok.Getter;
import lombok.Setter;
import managers.ReviewManager;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Date;
import java.util.List;

@ManagedBean
@SessionScoped
public class ReviewController {

    private @Setter @Getter Reviews newReview = new Reviews();

    @EJB
    ReviewManager reviewManager;

    public void createReview(Book book){
        newReview.setBook(reviewManager.getBookById(book.getId()));
        newReview.setDate(new Date());
//        book.getReviews().add(newReview);
        reviewManager.createReview(newReview);
    }

    public List<Reviews> getReviews(Book book) {
        return reviewManager.getBookById(book.getId()).getReviews();
    }
}
