package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
public class BookDto implements Serializable {

    private @Getter @Setter
    Long id;

    private @Getter @Setter
    String isbn;

    private @Getter @Setter
    String name;

    private @Getter @Setter
    String publisher;

    private @Getter @Setter
    int publishYear;

    private @Getter @Setter
    Float averageRating;

    private @Getter @Setter
    LocalDateTime createDate;

    private @Getter @Setter
    List<Long> authors = new ArrayList<>();
}
