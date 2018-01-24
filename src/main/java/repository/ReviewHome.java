package repository;

import entity.Reviews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class ReviewHome extends AbstractHome<Reviews> {
    final Logger logger = LoggerFactory.getLogger(ReviewHome.class);

    ReviewHome() {
        super(Reviews.class);
    }

}
