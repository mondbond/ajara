package rest;

import data.entity.Author;
import repository.AuthorFacade;
import repository.AuthorHome;
import rest.dto.AuthorDto;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("/author")
public class AuthorRest {

    @EJB
    AuthorFacade authorFacade;

    @EJB
    AuthorHome authorHome;

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

    @GET
    @Path("/delete/{pk}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteByPk(@PathParam(value = "pk") Long pk) {
        authorHome.deleteByPk(pk);
        return Response.status(200).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/add/")
    public Response createAuthor(@FormParam("name") String name,
                                 @FormParam("second_name") String secondName) {
        authorHome.insert(new Author(name, secondName, new Date()));
        return Response.status(200).build();
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void updateAuthor(@FormParam("pk") Long id,
                             @FormParam("name") String name,
                             @FormParam("second_name") String secondName) {
        Author author = authorFacade.findByPk(id);
        authorHome.update(author);
    }
}
