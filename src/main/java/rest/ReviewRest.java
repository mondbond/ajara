package rest;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;
import rest.dto.ReviewDto;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/reviews")
public class ReviewRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRest.class);

    @EJB
    private
    BookFacade bookFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pk}")
    public List<ReviewDto> getReviewsByBookPk(@PathParam("pk") String bookId) {

        List<ReviewDto> reviews = bookFacade.findByPk(Long.parseLong(bookId)).getReviews().stream()
                .map(r -> new ReviewDto(r.getId(), r.getCommenterName(), r.getCom(), r.getRating(), r.getCreateDate()))
                .collect(Collectors.toList());
        if (!ObjectUtils.allNotNull(reviews)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return reviews;
    }
}
