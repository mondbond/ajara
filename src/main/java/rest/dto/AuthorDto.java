package rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorDto implements Serializable {

    public AuthorDto(long id, String firstName, String secondName, Date createDate, List<Long> books) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.createDate = createDate;
        this.books = books;
    }

    private @Getter long id;

    private @Getter @Setter String firstName;

    private @Getter @Setter String secondName;

    private @Getter @Setter Float avgRating;

    private @Getter @Setter Date createDate;

    private @Getter @Setter List<Long> books = new ArrayList<>();
}
