package repository;

import data.entity.Author;

import javax.ejb.Stateless;

@Stateless
public class AuthorFacade extends AbstractFacade<Author> {

    AuthorFacade() {
        super(Author.class);
    }
}
