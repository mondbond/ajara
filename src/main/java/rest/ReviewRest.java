package rest;

import repository.BookFacade;
import repository.ReviewFacade;
import rest.dto.ReviewDto;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/review")
public class ReviewRest {

    @EJB
    ReviewFacade reviewFacade;

    @EJB
    BookFacade bookFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookId}")
    public List<ReviewDto> getReviewsByPk(@PathParam("bookId") String bookId){
       return bookFacade.findByPk(Long.parseLong(bookId)).getReviews().stream()
               .map(r -> new ReviewDto(r.getId(), r.getCommenterName(), r.getCom(), r.getRating(), r.getCreateDate()))
               .collect(Collectors.toList());
    }
}
