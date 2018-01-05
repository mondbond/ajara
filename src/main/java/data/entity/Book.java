package data.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "BOOK")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pp")
    @SequenceGenerator(name="pp", allocationSize = 1, sequenceName="BOOK_SEQ")
    private @Getter @Setter
    Long id;

    @Column(name = "ISBN")
    @Pattern(regexp = "[0-9]{10,18}", message = "ISBN must have minimum 10, maximum 18 numbers")
    private @Getter @Setter String isbn;

    @Column(name = "NAME")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,50}", message = "Name must contain minimum 3 maximum 50 characters")
    private @Getter @Setter String name;

    @Column(name = "PUBLISHER")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,50}", message = "PUBLISHER must contain minimum 3 maximum 50 characters")
    private @Getter @Setter String publisher;

    @Column(name = "PUBLISH_YEAR")
    @Pattern(regexp = "[0-9]{4}", message = "Year must contain only 4 numbers")
    private @Getter @Setter int publishYear;

    @Column(name = "AVG_RATING")
    private @Getter @Setter Float avgRating;

    @Column(name = "CREATE_DATE")
    private @Getter @Setter Date crateDate;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "JOIN_BOOK_AUTHOR",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn (name = "author_id")})
    private @Getter @Setter List<Author> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade=CascadeType.ALL)
    private @Getter @Setter List<Reviews> reviews;

    @Override
    public String toString() {
        return "Book";
    }
}