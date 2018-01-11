package rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
public class BookDto implements Serializable {

    public BookDto(Long id, String isbn, String name, String publisher, int publishYear, Float avgRating, Date crateDate, List<Long> authors) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.avgRating = avgRating;
        this.crateDate = crateDate;
        this.authors = authors;
    }

    private @Getter @Setter Long id;

    private @Getter @Setter String isbn;

    private @Getter @Setter String name;

    private @Getter @Setter String publisher;

    private @Getter @Setter int publishYear;

    private @Getter @Setter Float avgRating;

    private @Getter @Setter
    Date crateDate;

    private @Getter @Setter
    List<Long> authors = new ArrayList<>();
}
