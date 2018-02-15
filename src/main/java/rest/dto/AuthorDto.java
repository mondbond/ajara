package rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public class AuthorDto implements Serializable {

    private @Getter
    long id;

    private @Getter @Setter
    String firstName;

    private @Getter @Setter
    String secondName;

    private @Getter @Setter
    Float averageRating;

    private @Getter @Setter
    LocalDateTime createDate;

    private @Getter @Setter
    List<Long> books = new ArrayList<>();
}
