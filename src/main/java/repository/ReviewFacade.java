package repository;

import entity.Reviews;
import javax.ejb.Stateless;

@Stateless
public class ReviewFacade extends AbstractFacade<Reviews> {

    ReviewFacade() {
        super(Reviews.class);
    }
}
