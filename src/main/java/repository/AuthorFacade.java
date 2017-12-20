package repository;

import data.entity.Author;

import javax.ejb.Stateful;

@Stateful
public class AuthorFacade extends AbstractFacade<Author>{

    AuthorFacade() {
        super(Author.class);
    }

}
