package repository;

import entity.Book;
import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BookFacade extends AbstractFacade<Book> {

    public BookFacade() {
        super(Book.class);
    }

    public List<Book> filterByRating(Integer rating){
        Query query = getEntityManager().createNamedQuery(Book.QUERY_BY_RATING);
        query.setParameter(1, rating);
        query.setParameter(2, rating - 1);
        return (List<Book>) query.getResultList();
    }

    public Long getCountByRating(Long rating){
        Query query = getEntityManager().createNamedQuery(Book.QUERY_COUNT_BY_RATING);
//        query.setParameter(1, new Float(4));
//        query.setParameter(2,  new Float(2));
        return (Long) query.getSingleResult();
    }
}
