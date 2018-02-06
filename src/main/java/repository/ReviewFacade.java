package repository;

import entity.Reviews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class ReviewFacade extends AbstractFacade<Reviews> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewFacade.class);

    ReviewFacade() {
        super(Reviews.class);
    }
}
