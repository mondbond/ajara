package managers;

import entity.Author;
import repository.AuthorFacade;
import repository.AuthorHome;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AuthorManager {

    @EJB
    private AuthorFacade authorFacade;

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
//        ids.forEach(this::delete); // TODO: replace with query (?)
//        Stream.of(ids).flatMap(pk->ids.stream()).forEach(pk->delete(pk));
    }

    public List<Author> getAutocompleteBySecondName(String characters){
        System.out.println("!@@@@" + authorFacade.getAutocompleteBySecondName(characters).toString());
        return authorFacade.getAutocompleteBySecondName(characters);
    }
}
