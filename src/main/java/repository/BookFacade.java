package repository;

import data.entity.Book;
import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BookFacade extends AbstractFacade<Book> {

    public BookFacade() {
        super(Book.class);
    }

    public List<Book> filterByRating(Integer rating){
        Query query = getEntityManager().createNamedQuery("Book.eq.ratinq");
        query.setParameter(1, rating);
        query.setParameter(2, rating - 1);
        return (List<Book>) query.getResultList();
    }

    public Long getCountByRating(Long rating){
        Query query = getEntityManager().createNamedQuery("Book.count.eq.ratinq");
//        query.setParameter(1, String.valueOf(rating));
//        query.setParameter(2,  String.valueOf(rating - 1));
        return (Long) query.getSingleResult();
    }
}
