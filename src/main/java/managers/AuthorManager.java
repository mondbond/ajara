package managers;

import entity.Author;
import exception.AuthorException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AuthorFacade;
import repository.AuthorHome;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AuthorManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorManager.class);

    @EJB
    private @Getter AuthorFacade authorFacade;

    @EJB
    private AuthorHome authorHome;

    public Author getAuthorByPk(long pk) throws AuthorException {
        try {
            return authorFacade.findByPk(pk);
        }catch (Exception e){
            throw new AuthorException("Something happen while you trying to find author", e);
        }
    }

    public List<Author> getAuthorsByPk(List<Long> pk) throws AuthorException {
        try {
            return (List<Author>) authorFacade.findByPks(pk);
        }catch (Exception e){
            throw new AuthorException("Something happen while you trying to find authors", e);
        }
    }

    public List<Author> getAllAuthors() throws AuthorException {
        try {
            return authorFacade.findAll();
        }catch (Exception e){
            throw new AuthorException("Something happen while you trying to find authors", e);
        }
    }

    public void save(Author author) throws AuthorException {
        try {
            authorHome.insert(author);
        }catch (Exception e){
            throw new AuthorException("bla", e);
        }
    }

    public void update(Author author) throws AuthorException {
        try {
            authorHome.update(author);
        }catch (Exception e){
            throw new AuthorException("Something happen while you trying to update author", e);
        }
    }

    public void delete(long pk) throws AuthorException {
        try {
            authorHome.deleteByPk(pk);
        }catch (Exception e){
            throw new AuthorException("Something happen while you trying to delete author", e);
        }
    }

    public void deleteList(List<Long> ids) throws AuthorException {
        try {
            authorHome.deleteList(ids);
        }catch (Exception e){
            throw new AuthorException("Something happen while you trying to delete authors", e);
        }
    }

    public List<Author> getAutocompleteBySecondName(String prefix) throws AuthorException {
        try {
            LOGGER.info("IN getAutocompleteBySecondName(prefix = [{}])", prefix);
            List<Author> result = authorFacade.getEntityManager().createNamedQuery(Author.QUERY_LIKE_SECOND_NAME, Author.class)
                    .setParameter(1, prefix + "%")
                    .getResultList();
            LOGGER.debug("OUT getAutocompleteBySecondName: returned [{}]", result);
            return result;
        }catch (Exception e){
            throw new AuthorException("Something happen while you trying to get authors autocomplete", e);
        }
    }
}
