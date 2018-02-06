package repository;

import entity.Reviews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class ReviewHome extends AbstractHome<Reviews> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewHome.class);

    ReviewHome() {
        super(Reviews.class);
    }

}
