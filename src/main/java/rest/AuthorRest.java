package rest;

import entity.Author;
import exception.AuthorException;
import managers.AuthorManager;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.dto.AuthorDto;
import rest.mappers.AuthorMapper;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/authors")
public class AuthorRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRest.class);

    private AuthorMapper authorMapper = new AuthorMapper();

    @EJB
    private AuthorManager authorManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getAllAuthors() throws AuthorException {
        LOGGER.info("getAuthorByPk(pk = [{}])");
        List<AuthorDto> list = authorManager.getAllAuthors().stream().map(authorMapper).collect(Collectors.toList());
        return (CollectionUtils.isEmpty(list)) ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.status(Response.Status.OK).entity(list).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pk}")
    public Response getAuthorByPk(@PathParam(value = "pk") Long pk) throws AuthorException {
        LOGGER.info("getAuthorByPk(pk = [{}])", pk);
        Author author = authorManager.getAuthorByPk(pk);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(authorMapper.apply(author)
        ).build();
    }

    @DELETE
    @Path("/{pk}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteByPk(@PathParam(value = "pk") Long pk) throws AuthorException {
        LOGGER.info("deleteByPk(pk = [{}])", pk);
        Author author = authorManager.getAuthorByPk(pk);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        authorManager.delete(pk);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/")
    public Response createAuthor(@FormParam("name") String name,
                                 @FormParam("second_name") String secondName) {
        LOGGER.info("createAuthor(name = [{}]), second name = [{}]", name, secondName);
        try {
            authorManager.save(new Author(name, secondName));
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateAuthor(@FormParam("pk") Long id,
                                 @FormParam("name") String name,
                                 @FormParam("second_name") String secondName) throws AuthorException {
        LOGGER.info("updateAuthor(name = [{}]), second name = [{}], pk = [{}]", name, secondName, id);
        Author author = authorManager.getAuthorByPk(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        author.setFirstName(name);
        author.setSecondName(secondName);
        try {
            authorManager.update(author);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}
