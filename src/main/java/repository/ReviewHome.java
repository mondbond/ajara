package repository;

import data.entity.Reviews;

import javax.ejb.Stateless;

@Stateless
public class ReviewHome extends AbstractHome<Reviews> {

    ReviewHome() {
        super(Reviews.class);
    }

}
