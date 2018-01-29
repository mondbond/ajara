package model;

import entity.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.JPADataModel;

import javax.persistence.EntityManager;

public class AuthorJPAModel extends JPADataModel<Author> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorJPAModel.class);


    public AuthorJPAModel(EntityManager entityManager, String defaultSortingColumn, Class<Author> entityClass) {
        super(entityManager, defaultSortingColumn, entityClass);
    }

    @Override
    protected Object getId(Author author) {
        return author.getId();
    }
}
