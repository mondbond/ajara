package repository;

import entity.Author;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AuthorFacade extends AbstractFacade<Author> {

    AuthorFacade() {
        super(Author.class);
    }

    public List<Author> getAutocompleteBySecondName(String characters){
        return (List<Author>) getEntityManager().createNamedQuery(Author.QUERY_LIKE_SECOND_NAME).setParameter(1, characters).getResultList();
    }
}
