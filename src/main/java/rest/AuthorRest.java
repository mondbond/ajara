package rest;

import entity.Author;
import entity.Book;
import exception.AuthorException;
import managers.AuthorManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
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
        LOGGER.info("getAuthorByPk(pk = [{}])");
        List<Author> list = authorManager.getAllAuthors();
        if (CollectionUtils.isEmpty(list)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return Response.status(Response.Status.OK).entity(list).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pk}")
    public AuthorDto getAuthorByPk(@PathParam(value = "pk") Long pk) throws AuthorException {
        LOGGER.info("getAuthorByPk(pk = [{}])", pk);
        Author author = authorManager.getAuthorByPk(pk);
        if (!ObjectUtils.allNotNull(author)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new AuthorDto(author.getId(), author.getFirstName(),
                author.getSecondName(), author.getCreateDate(),
                author.getBooks().stream()
                        .map(Book::getId)
                        .collect(Collectors.toList()));
    }

    @DELETE
    @Path("/{pk}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteByPk(@PathParam(value = "pk") Long pk) throws AuthorException {
        LOGGER.info("deleteByPk(pk = [{}])", pk);
        Author author = authorManager.getAuthorByPk(pk);
        if (!ObjectUtils.allNotNull(author)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        authorManager.delete(pk);

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/")
    public Response createAuthor(@FormParam("name") String name,
                                 @FormParam("second_name") String secondName) throws AuthorException {
        LOGGER.info("createAuthor(name = [{}]), second name = [{}]", name, secondName);
        try {
            authorManager.save(new Author(name, secondName));
        } catch (Exception e) {
            throw new WebApplicationException();
        }

        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateAuthor(@FormParam("pk") Long id,
                                 @FormParam("name") String name,
                                 @FormParam("second_name") String secondName) throws AuthorException {
        LOGGER.info("updateAuthor(name = [{}]), second name = [{}]", name, secondName);
        Author author = authorManager.getAuthorByPk(id);
        if (!ObjectUtils.allNotNull(author)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        try {
            authorManager.update(author);
        } catch (Exception e) {
            throw new WebApplicationException();
        }

        return Response.status(Response.Status.OK).build();
    }
}
