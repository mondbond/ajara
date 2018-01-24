package repository;

import entity.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class AuthorHome extends AbstractHome<Author> {
    final Logger logger = LoggerFactory.getLogger(AuthorHome.class);

    AuthorHome() {
        super(Author.class);
    }
}
