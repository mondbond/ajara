package repository;

import data.entity.Author;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.bean.SessionScoped;

@Stateless
public class AuthorHome extends AbstractHome<Author> {

    AuthorHome() {
        super(Author.class);
    }
}
