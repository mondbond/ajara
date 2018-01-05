package rest;

import data.entity.Author;
import repository.AuthorFacade;
import rest.dto.AuthorDto;
import rest.dto.ReviewDto;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Path("/author")
public class AuthorRest {

    @EJB
    AuthorFacade authorFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getAllAuthors() {
        List<Author> list = authorFacade.findAll();
        return Response.status(200).entity(list).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pk}")
    public AuthorDto getAuthorByPk(@PathParam(value = "pk") Long pk){
        Author author =  authorFacade.findByPk(pk);
        return  new AuthorDto(author.getId(), author.getFirstName(),
                author.getSecondName(), author.getCreateDate(),
                author.getBooks().stream()
                        .map(b -> b.getId())
                        .collect(Collectors.toList()));
    }
}
