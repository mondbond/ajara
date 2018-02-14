package rest;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public List<BookDto> getBooks(@QueryParam("skip") int skip, @QueryParam("limit") int limit,
                                  @QueryParam("sort") String sortColumn, @QueryParam("isAsc") boolean isAsc) {
        List<BookDto> books =  bookFacade.getPagination(skip, limit, sortColumn, isAsc).stream()
                .map(bm).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(books)){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return books;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pk}")
    public BookDto getBook(@PathParam("pk") Long bookId) {

        BookDto book = Stream.of(bookFacade.findByPk(bookId)).map(bm).findFirst().get();
        if(!ObjectUtils.allNotNull(book)){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Stream.of(bookFacade.findByPk(bookId)).map(bm).findFirst().get();
    }
}
