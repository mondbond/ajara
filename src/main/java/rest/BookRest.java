package rest;

import entity.Book;
import util.exception.BookException;
import managers.BookManager;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.dto.BookDto;
import rest.mappers.BookMapper;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/books")
public class BookRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookRest.class);
    private BookMapper bookMapper = new BookMapper();

    @EJB
    private BookManager bookManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getAllBooks(@QueryParam("skip") int skip, @QueryParam("limit") int limit,
                                @QueryParam("sort") String sortColumn, @QueryParam("isAsc") boolean isAsc)
            throws BookException {
        try {
            List<BookDto> books = bookManager.getPagination(skip, limit, sortColumn, isAsc).stream()
                .map(bookMapper).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(books)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return (CollectionUtils.isEmpty(books)) ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.status(Response.Status.OK).entity(books).build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pk}")
    public Response getBookByPk(@PathParam("pk") Long pk) throws BookException {
        Book book = bookManager.getBookByPk(pk);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(bookMapper.apply(book)).build();
    }

    @DELETE
    @Path("/{pk}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteByPk(@PathParam(value = "pk") Long pk) throws BookException {
        LOGGER.info("deleteByPk(pk = [{}])", pk);
        Book book = bookManager.getBookByPk(pk);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        bookManager.delete(pk);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/")
    public Response createBook(@FormParam("name") String name,
                               @FormParam("publisher") String publisher,
                               @FormParam("isbn") String isbn,
                               @FormParam("year") int year
    ) {
        LOGGER.info("createBook(name = [{}]), publisher = [{}], isbn = [{}], year = [{}])", name, publisher, isbn, year);
        try {
            bookManager.save(Book.builder().name(name).publisher(publisher).isbn(isbn).publishYear(year).build());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateBook(@FormParam("pk") Long pk,
                               @FormParam("name") String name,
                               @FormParam("publisher") String publisher,
                               @FormParam("isbn") String isbn,
                               @FormParam("year") int year
    ) throws BookException {
        LOGGER.info("updateBook (name = [{}]), publisher = [{}], isbn = [{}], year = [{}])", name, publisher, isbn, year);
        Book book = bookManager.getBookByPk(pk);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        book.setName(name);
        book.setPublisher(publisher);
        book.setIsbn(isbn);
        book.setPublishYear(year);

        try {
            bookManager.update(book);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}
