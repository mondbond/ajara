package data.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "AUTHOR")
public class Author implements Serializable {

    public Author() {}

    public Author(String firstName, String secondName, Date createDate) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="purchase_seq")
    @SequenceGenerator(name="purchase_seq", allocationSize = 1, sequenceName="AJARA_SEQ")
    private @Getter @Setter long id;

    @Column(name = "FIRST_NAME")
    private @Getter @Setter String firstName;

    @Column(name = "SECOND_NAME")
    private @Getter @Setter String secondName;

    @Column(name = "CREATE_DATE")
    private @Getter @Setter Date createDate;

    @ManyToMany(mappedBy = "authors")
    private @Getter @Setter List<Book> books = new ArrayList<>();
}
