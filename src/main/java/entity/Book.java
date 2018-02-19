package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "authors", callSuper = true)
@Getter
@Setter
@Entity
@SequenceGenerator(name = "variable_sequence", allocationSize = 1, sequenceName = "BOOK_PK_SEQ")
@Table(name = "BOOK", uniqueConstraints = {@UniqueConstraint(columnNames = {"isbn"})})
@NamedQueries({
        @NamedQuery(name = Book.QUERY_COUNT_BY_RATING,
                query = "select count(*) from Book b WHERE b.averageRating > ?1 and b.averageRating <= ?2")
})
public class Book extends BaseEntity implements Serializable {
    public static String PK_COLUMN = "ID";
    public static String NAME_COLUMN = "NAME";
    public static String AUTHOR_COLUMN = "AUTHOR";
    public static String ISBN_COLUMN = "ISBN";
    public static String PUBLISHER_COLUMN = "PUBLISHER";
    public static String YEAR_COLUMN = "PUBLISH_YEAR";
    public static String AVG_RATING_COLUMN = "AVERAGE_RATING";
    public static String DATE_COLUMN = "CREATE_DATE";

    public static final String QUERY_COUNT_BY_RATING = "Book.count.eq.ratinq";

    @Column(name = "ISBN", unique = true)
    @Pattern(regexp = "[0-9]{10,18}", message = "ISBN must contain minimum 10, maximum 18 numbers and be unique")
    private String isbn;

    @Column(name = "NAME")
    @Pattern(regexp = "[a-zA-Z0-9- ]{3,50}", message = "Name must contain minimum 3 maximum 50 characters without special characters")
    private String name;

    @Column(name = "PUBLISHER")
    @Pattern(regexp = "[a-zA-Z0-9- ]{3,50}", message = "PUBLISHER must contain minimum 3 maximum 50 characters without special characters")
    private String publisher;

    @Column(name = "PUBLISH_YEAR")
    @Range(min = 1000, max = 2018, message = "Year must be between 1000 and 2018")
    private Integer publishYear;

    @Column(name = "AVERAGE_RATING")
    private Float averageRating;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "JOIN_BOOK_AUTHOR",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private List<Author> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Review> reviews;
}
