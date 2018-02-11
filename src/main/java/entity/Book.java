package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entity.listeners.CreateDateListener;
import entity.listeners.HasDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@EntityListeners({CreateDateListener.class})
@Table(name = "BOOK", uniqueConstraints={@UniqueConstraint(columnNames={"isbn"})})
@NamedQueries({
        @NamedQuery(name = Book.QUERY_COUNT_BY_RATING,
                query = "select count(*) from Book b WHERE b.avgRating > ?1 and b.avgRating <= ?2")
})
public class Book implements Serializable, HasDate {
    public static final @Getter String PK_COLUMN = "ID";
    public static final @Getter String NAME_COLUMN = "NAME";
    public static final @Getter String AUTHOR_COLUMN = "AUTHOR";
    public static final @Getter String ISBN_COLUMN = "ISBN";
    public static final @Getter String PUBLISHER_COLUMN = "PUBLISHER";
    public static final @Getter String YEAR_COLUMN = "PUBLISH_YEAR";
    public static final @Getter String AVG_RATING_COLUMN = "AVG_RATING";
    public static final @Getter String DATE_COLUMN = "CREATE_DATE";

    public static final String QUERY_COUNT_BY_RATING = "Book.count.eq.ratinq";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_id_sequence")
    @SequenceGenerator(name="book_id_sequence", allocationSize = 1, sequenceName="BOOK_SEQ")
    private Long id;

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

    @Column(name = "AVG_RATING")
    private Float avgRating;

    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "JOIN_BOOK_AUTHOR",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn (name = "author_id")})
    private List<Author> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private List<Review> reviews;

    @Override
    public String toString() {
        return "Book";
    }

    @Override
    public void setDate(LocalDate date) {
        createDate = date;
    }
}
