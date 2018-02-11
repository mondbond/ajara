package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entity.listeners.CreateDateListener;
import entity.listeners.HasDate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@EqualsAndHashCode
@EntityListeners({CreateDateListener.class})
@Table(name = "AUTHOR")
@NamedQuery(name = "Author.by.secondName.like",
            query = "select a from Author a WHERE a.secondName LIKE ?1")
public class Author implements Serializable, HasDate {
    public static @Getter final String PK_COLUMN = "ID";
    public static @Getter final String NAME_COLUMN = "FIRST_NAME";
    public static @Getter final String SECOND_NAME_COLUMN = "SECOND_NAME";
    public static @Getter final String AVG_RATING_COLUMN = "AVG_RATING";
    public static @Getter final String DATE_COLUMN = "CREATE_DATE";

    public static final String QUERY_LIKE_SECOND_NAME = "Author.by.secondName.like";

    public Author(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="author_id_sequence")
    @SequenceGenerator(name="author_id_sequence", allocationSize = 1, sequenceName="AJARA_SEQ")
    private long id;

    @Column(name = "FIRST_NAME")
    @Pattern(regexp = "[a-zA-Z0-9- ]{3,100}", message = "Name must contain minimum 3 maximum 100 characters without special symbols")
    private String firstName;

    @Column(name = "SECOND_NAME")
    @Pattern(regexp = "[a-zA-Z0-9- ]{3,100}", message = "Second name must contain minimum 3 maximum 100 characters without special symbols")
    private String secondName;

    @Column(name = "AVG_RATING")
    private Float avgRating; // TODO: Rename to averageRating

    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    public String fullName() {
        return secondName + " " + firstName ;
    }

    @Override
    public void setDate(LocalDate date) {
        createDate = date;
    }
}
