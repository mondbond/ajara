package repository;

import entity.Review;

import javax.ejb.Stateless;

@Stateless
public class ReviewFacade extends AbstractFacade<Review> {

    ReviewFacade() {
        super(Review.class);
    }
}
