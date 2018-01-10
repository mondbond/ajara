package managers;

import data.entity.Author;
import org.ajax4jsf.model.ExtendedDataModel;
import repository.AuthorFacade;
import repository.AuthorHome;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class AuthorManager {

    @EJB
    private AuthorFacade authorFacade;

    @EJB
    private AuthorHome authorHome;

    public Author getAuthorByPk(long pk) {
        return authorFacade.findByPk(pk);
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
        Stream.of(ids).flatMap(pk->ids.stream()).forEach(pk->delete(pk));
    }

    public List<Author> getAutocompleteBySecondName(String characters){
        return authorFacade.getAutocompleteBySecondName(characters);
    }
}
