package model;

import entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.JPADataModel;

import javax.persistence.EntityManager;

public class BookJPAModel extends JPADataModel<Book> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookJPAModel.class);

    public BookJPAModel(EntityManager entityManager, String defaultSortingColumn, Class<Book> entityClass) {
        super(entityManager, defaultSortingColumn, entityClass);
    }

    @Override
    protected Object getId(Book book) {
        return book.getId();
    }
}
