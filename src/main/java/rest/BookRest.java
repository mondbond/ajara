package rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;
import rest.dto.BookDto;
import rest.mappers.BookMapper;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/book")
public class BookRest {
    final Logger logger = LoggerFactory.getLogger(BookRest.class);
    private BookMapper bm = new BookMapper();

    @EJB
    private BookFacade bookFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sort/{sort}")
    public List<BookDto> getAllBooks(@PathParam("sort") String sortColumn) {
        return bookFacade.getPagination(0, 100, sortColumn, false).stream()
                .map(bm).collect(Collectors.toList());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookId}")
    public BookDto getAllBooks(@PathParam("bookId") Long bookId) {
        return Stream.of(bookFacade.findByPk(bookId)).map(bm).findFirst().get();
    }
}
