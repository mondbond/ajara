package rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReviewDto implements Serializable {

    public ReviewDto(Long id, String commenterName, String com, int rating, LocalDateTime date) {
        this.id = id;
        this.commenterName = commenterName;
        this.com = com;
        this.rating = rating;
        this.date = date;
    }

    private @Getter @Setter
    Long id;

    private @Getter @Setter
    String commenterName;

    private @Getter @Setter
    String com;

    private @Getter @Setter
    int rating;

    private @Getter @Setter
    LocalDateTime date;
}
