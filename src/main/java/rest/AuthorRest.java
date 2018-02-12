package rest;

import entity.Author;
import entity.Book;
import exception.AuthorException;
import managers.AuthorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.dto.AuthorDto;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/authors")
public class AuthorRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRest.class);

    @EJB
    private AuthorManager authorManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getAllAuthors() throws AuthorException {
        List<Author> list = authorManager.getAllAuthors();
        return Response.status(200).entity(list).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pk}")
    public AuthorDto getAuthorByPk(@PathParam(value = "pk") Long pk) throws AuthorException {
        Author author =  authorManager.getAuthorByPk(pk);
        return  new AuthorDto(author.getId(), author.getFirstName(),
                author.getSecondName(), author.getCreateDate(),
                author.getBooks().stream()
                        .map(Book::getId)
                        .collect(Collectors.toList()));
    }

    @DELETE
    @Path("/{pk}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteByPk(@PathParam(value = "pk") Long pk) throws AuthorException {
        authorManager.delete(pk);
        return Response.status(200).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/")
    public Response createAuthor(@FormParam("name") String name,
                                 @FormParam("second_name") String secondName) throws AuthorException {
        authorManager.save(new Author(name, secondName));
        return Response.status(200).build();
    }

    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void updateAuthor(@FormParam("pk") Long id,
                             @FormParam("name") String name,
                             @FormParam("second_name") String secondName) throws AuthorException {
        Author author = authorManager.getAuthorByPk(id);
        authorManager.update(author);
    }
}
