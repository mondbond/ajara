package repository;

import data.entity.Author;

import javax.ejb.Stateful;

@Stateful
public class AuthorHome extends AbstractHome<Author> {

    AuthorHome() {
        super(Author.class);
    }
}
