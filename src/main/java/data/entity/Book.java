package data.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pp")
    @SequenceGenerator(name="pp", allocationSize = 1, sequenceName="BOOK_SEQ")
    private @Getter @Setter
    Long id;

    @Column(name = "ISBN")
    private @Getter @Setter String isbn;

    @Column(name = "NAME")
    private @Getter @Setter String name;

    @Column(name = "PUBLISHER")
    private @Getter @Setter String publisher;

    @Column(name = "PUBLISH_YEAR")
    private @Getter @Setter int publishYear;

    @Column(name = "CREATE_DATE")
    private @Getter @Setter Date crateDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "JOIN_BOOK_AUTHOR",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn (name = "author_id")})
    private @Getter @Setter List<Author> authors = new ArrayList<>();
}
