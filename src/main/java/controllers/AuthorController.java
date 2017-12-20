package controllers;

import data.entity.Author;
import managers.AuthorManager;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ManagedBean(name = "authorcon")
@ViewScoped
public class AuthorController {

    @EJB
    AuthorManager authorManager;

    public Author author = new Author();

    public String getAuthorName(){
        String name = authorManager.getAuthorByPk(1).getFirstName();
        return name;
    }

    public String getAuthorFullNameByPk(){
        Author author = authorManager.getAuthorByPk((long)1);
        return author.getFirstName() + " " + author.getSecondName();
    }

    public List<Author> getAllAuthors(){
        List<Author> list = authorManager.getAllAuthors();
        Stream.of(list).flatMap((authors)-> authors.stream() ).forEach( (author) -> System.out.println(author.getSecondName()));
        return authorManager.getAllAuthors();
    }

    public void newAuthor() {
        authorManager.save(new Author(author.getFirstName(), author.getSecondName(), new Date()));
        author = new Author();
    }


//    getset
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
