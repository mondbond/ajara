package rest;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;
import rest.dto.ReviewDto;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    public Response getReviewsByBookPk(@PathParam("pk") String pk) {

        List<ReviewDto> reviews = bookFacade.findByPk(Long.parseLong(pk)).getReviews().stream()
                .map(r -> new ReviewDto(r.getId(), r.getCommenterName(), r.getCommentText(), r.getRating(), r.getCreateDate()))
                .collect(Collectors.toList());
        if (!ObjectUtils.allNotNull(reviews)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(reviews).build();
    }
}
