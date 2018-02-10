package repository;

import entity.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class AuthorFacade extends AbstractFacade<Author> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFacade.class);

    public AuthorFacade() {
        super(Author.class);
    }

}
