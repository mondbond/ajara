package repository;

import data.entity.Author;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.bean.SessionScoped;

@Stateless
public class AuthorFacade extends AbstractFacade<Author>{

    AuthorFacade() {
        super(Author.class);
    }
}
