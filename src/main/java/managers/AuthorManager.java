package managers;

import entity.Author;
import exception.AuthorException;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AuthorFacade;
import repository.AuthorHome;

import javax.ejb.*;
import java.util.List;

import static javax.ejb.TransactionAttributeType.REQUIRED;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = REQUIRED)
public class AuthorManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorManager.class);

    @EJB
    private AuthorFacade authorFacade;

    @EJB
    private @Setter
    AuthorHome authorHome;

    public Author getAuthorByPk(long pk) throws AuthorException {
        try {
            return authorFacade.findByPk(pk);
        } catch (Exception e) {
            throw new AuthorException("Enable to find author", e);
        }
    }

    public List<Author> getAuthorsByPk(List<Long> pk) throws AuthorException {
        try {
            return authorFacade.findByPks(pk);
        } catch (Exception e) {
            throw new AuthorException("Enable to find authors", e);
        }
    }

    public List<Author> getAllAuthors() throws AuthorException {
        try {
            return authorFacade.findAll();
        } catch (Exception e) {
            throw new AuthorException("Enable to find authors", e);
        }
    }

    public void save(Author author) throws AuthorException {
        try {
            authorHome.insert(author);
        } catch (Exception e) {
            throw new AuthorException("Enable to insert author", e);
        }
    }

    public void update(Author author) throws AuthorException {
        try {
            authorHome.update(author);
        } catch (Exception e) {
            throw new AuthorException("Enable to update author", e);
        }
    }

    public void delete(long pk) throws AuthorException {
        try {
            authorHome.deleteByPk(pk);
        } catch (Exception e) {
            throw new AuthorException("Enable to delete author", e);
        }
    }

    public void deleteList(List<Long> ids) throws AuthorException {
        try {
            authorHome.deleteByPkList(ids);
        } catch (Exception e) {
            throw new AuthorException("Enable to delete authors", e);
        }
    }

    public List<Author> getAutocompleteBySecondName(String prefix) throws AuthorException {
        try {
            return authorFacade.getAutocompleteByColumn(prefix);
        } catch (Exception e) {
            throw new AuthorException("Enable to get authors autocomplete", e);
        }
    }

    public List<Author> getPagination(int skip, int limit, String sortColumn, boolean isAsc) throws AuthorException {
        try {
            return authorFacade.getPagination(skip, limit, sortColumn, isAsc);
        } catch (Exception e) {
            throw new AuthorException("Enable to get authors autocomplete", e);
        }
    }

    public int countAll() throws AuthorException {
        try {
            return authorFacade.countAll();
        } catch (Exception e) {
            throw new AuthorException("Enable to get authors autocomplete", e);
        }
    }
}
