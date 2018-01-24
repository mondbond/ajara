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

    /**
     * Method return authors list by prefix of second name
     * @param prefix prefix of authors second name
     * */
    public List<Author> getAutocompleteBySecondName(String prefix){
        LOGGER.info("IN getAutocompleteBySecondName:(entity = [{}], prefix = [{}])", Author.class, prefix);
        List<Author> result = getEntityManager().createNamedQuery(Author.QUERY_LIKE_SECOND_NAME, Author.class)
                .setParameter(1, prefix + "%")
                .getResultList();
        LOGGER.debug("OUT getAutocompleteBySecondName: returned [{}]", result);
        return result;
    }
}
