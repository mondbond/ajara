package repository;

import entity.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AuthorFacade extends AbstractFacade<Author> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFacade.class);

    public AuthorFacade() {
        super(Author.class);
    }

    public List<Author> getAutocompleteByColumn(String prefix) {
        LOGGER.info("IN getAutocompleteByColumn(prefix = [{}], column =[{}])", prefix);
        List<Author> result = getEntityManager().createNamedQuery(Author.QUERY_LIKE_SECOND_NAME, Author.class)
                .setParameter(1, prefix + "%")
                .getResultList();
        LOGGER.debug("OUT getAutocompleteBySecondName: returned [{}]", result);
        return result;
    }
}
