package data.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "BOOK")
public @Data class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pp")
    @SequenceGenerator(name="pp", allocationSize = 1, sequenceName="BOOK_SEQ")
    private Long id;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PUBLISHER")
    private String publisher;

    @Column(name = "PUBLISH_YEAR")
    private int publishYear;

    @Column(name = "CREATE_DATE")
    private Date crateDate;

    @ManyToMany
    @JoinTable(name = "JOIN_BOOK_AUTHOR",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn (name = "author_id")})
    private List<Author> authors = new ArrayList<>();
}
