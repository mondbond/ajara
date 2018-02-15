package rest;

import entity.Book;
import exception.BookException;
import managers.BookManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;
import rest.dto.BookDto;
import rest.mappers.BookMapper;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/books")
public class BookRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookRest.class);
    private BookMapper bm = new BookMapper();

    @EJB
    private BookFacade bookFacade;

    @EJB
    private BookManager bookManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public List<BookDto> getAllBooks(@QueryParam("skip") int skip, @QueryParam("limit") int limit,
                                     @QueryParam("sort") String sortColumn, @QueryParam("isAsc") boolean isAsc) {
        List<BookDto> books = bookFacade.getPagination(skip, limit, sortColumn, isAsc).stream()
                .map(bm).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(books)) {
            Response.status(Response.Status.NOT_FOUND).build();
        }
        return books;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pk}")
    public BookDto getBookByPk(@PathParam("pk") Long pk) {

        BookDto book = Stream.of(bookFacade.findByPk(pk)).map(bm).findFirst().get();
        if (!ObjectUtils.allNotNull(book)) {
            Response.status(Response.Status.NOT_FOUND).build();
        }
        return Stream.of(bookFacade.findByPk(pk)).map(bm).findFirst().get();
    }

    @DELETE
    @Path("/{pk}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteByPk(@PathParam(value = "pk") Long pk) throws BookException {
        LOGGER.info("deleteByPk(pk = [{}])", pk);
        Book book = bookManager.getBookByPk(pk);
        if (!ObjectUtils.allNotNull(book)) {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
            Response.status(Response.Status.NOT_FOUND).build();
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
    ) throws BookException {
        LOGGER.info("createBook(name = [{}]), publisher = [{}], isbn = [{}], year = [{}])", name, publisher, isbn, year);
        try {
            bookManager.save(Book.builder().name(name).publisher(publisher).isbn(isbn).publishYear(year).build());
        } catch (Exception e) {
            Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).build();
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
        if (!ObjectUtils.allNotNull(book)) {
            Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            bookManager.update(book);
        } catch (Exception e) {
            Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}
