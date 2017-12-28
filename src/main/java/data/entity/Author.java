package data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "AUTHOR")
public @Data class Author implements Serializable {

    public Author() {}

    public Author(String firstName, String secondName, Date createDate) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="purchase_seq")
    @SequenceGenerator(name="purchase_seq", allocationSize = 1, sequenceName="AJARA_SEQ")
    private long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "SECOND_NAME")
    private String secondName;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();
}
