package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = "books", callSuper = true)
@SequenceGenerator(name = "variable_sequence", allocationSize = 1, sequenceName = "AUTHOR_PK_SEQ")
@Table(name = "AUTHOR")
@NamedQuery(name = Author.QUERY_LIKE_SECOND_NAME,
        query = "select a from Author a WHERE a.secondName LIKE ?1")
public class Author extends BaseEntity {
    public static final String PK_COLUMN = "ID";
    public static final String NAME_COLUMN = "FIRST_NAME";
    public static final String SECOND_NAME_COLUMN = "SECOND_NAME";
    public static final String AVG_RATING_COLUMN = "AVERAGE_RATING";
    public static final String DATE_COLUMN = "CREATE_DATE";

    public static final String QUERY_LIKE_SECOND_NAME = "Author.by.secondName.like";

    public Author(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @Builder
    public Author(long id, String firstName, String secondName, Float averageRating, LocalDateTime createDate, List<Book> books) {
        super(id, createDate);
        this.firstName = firstName;
        this.secondName = secondName;
        this.averageRating = averageRating;
        this.books = books;
    }

    @Column(name = "FIRST_NAME")
    @Pattern(regexp = "[a-zA-Z0-9- ]{3,100}", message = "Name must contain minimum 3 maximum 100 characters without special symbols")
    private String firstName;

    @Column(name = "SECOND_NAME")
    @Pattern(regexp = "[a-zA-Z0-9- ]{3,100}", message = "Second name must contain minimum 3 maximum 100 characters without special symbols")
    private String secondName;

    @Column(name = "AVERAGE_RATING")
    private Float averageRating;

    @JsonIgnore
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    public String getFullName() {
        return secondName + " " + firstName;
    }
}
