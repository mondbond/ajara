package repository;

import entity.Review;

import javax.ejb.Stateless;

@Stateless
public class ReviewHome extends AbstractHome<Review> {

    ReviewHome() {
        super(Review.class);
    }

}
