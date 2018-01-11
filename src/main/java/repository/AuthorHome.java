package repository;

import entity.Author;

import javax.ejb.Stateless;

@Stateless
public class AuthorHome extends AbstractHome<Author> {

    AuthorHome() {
        super(Author.class);
    }
}
