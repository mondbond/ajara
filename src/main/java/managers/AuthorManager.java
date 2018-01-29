package managers;

import entity.Author;
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

    public Author getAuthorByPk(long pk) {
        return authorFacade.findByPk(pk);
    }

    public List<Author> getAuthorsByPk(List<Long> pk) {
        return (List<Author>) authorFacade.findByPks(pk);
    }

    public List<Author> getAllAuthors() {
        return authorFacade.findAll();
    }

    public void save(Author author) {
        authorHome.insert(author);
    }

    public void update(Author author) {
        authorHome.update(author);
    }

    public void delete(long pk){
        authorHome.deleteByPk(pk);
    }

    public void deleteList(List<Long> ids){
        authorHome.deleteList(ids);
    }

    public List<Author> getAutocompleteBySecondName(String characters){
        return authorFacade.getAutocompleteBySecondName(characters);
    }
}
