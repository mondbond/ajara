package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "AUTHOR")
@NamedQuery(name = "Author.by.secondName.like",
            query = "select a from Author a WHERE a.secondName LIKE ?1")
public class Author implements Serializable {

    public static final String QUERY_LIKE_SECOND_NAME = "Author.by.secondName.like";

//    lombok has no annotation for including only necessary fields
    public Author(String firstName, String secondName, Date createDate) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="author_id_sequence")
    @SequenceGenerator(name="author_id_sequence", allocationSize = 1, sequenceName="AJARA_SEQ")
    private long id;

    @Column(name = "FIRST_NAME")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,100}", message = "Name must contain minimum 3 maximum 100 characters")
    private String firstName;

    @Column(name = "SECOND_NAME")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,100}", message = "Second name must contain minimum 3 maximum 100 characters")
    private String secondName;

    @Column(name = "AVG_RATING")
    private Float avgRating; // TODO: Rename to averageRating

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

}
