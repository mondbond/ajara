package managers;


import data.entity.Author;
import repository.AuthorFacade;
import repository.AuthorHome;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean
@Stateful
public class AuthorManager {

    @EJB
    AuthorFacade authorFacade;

    @EJB
    AuthorHome authorHome;

    public Author getAuthorByPk(long pk) {
        return authorFacade.findByPk(pk);
    }

    public List<Author> getAllAuthors() {
        return authorFacade.findAll();
    }

    public void save(Author author) {
        authorHome.insert(author);
    }
}
