package data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "AUTHOR")
@NamedQuery(name = "Author.by.secondName.like", // TODO: Extract to public constant
            query = "select a from Author a WHERE a.secondName LIKE ?1")
public class Author implements Serializable {

    public Author() {} // TODO: replace with lombok annotation

    public Author(String firstName, String secondName, Date createDate) { // TODO: replace with lombok annotation
        this.firstName = firstName;
        this.secondName = secondName;
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="purchase_seq") // TODO: rename generator
    @SequenceGenerator(name="purchase_seq", allocationSize = 1, sequenceName="AJARA_SEQ")
    private @Getter @Setter long id; // TODO: Move getter setter to class level and remove from all fields

    @Column(name = "FIRST_NAME")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,100}", message = "Name must contain minimum 3 maximum 100 characters")
    private @Getter @Setter String firstName;

    @Column(name = "SECOND_NAME")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,100}", message = "Second name must contain minimum 3 maximum 100 characters")
    private @Getter @Setter String secondName;

    @Column(name = "AVG_RATING")
    private @Getter @Setter Float avgRating; // TODO: Rename to averageRating

    @Column(name = "CREATE_DATE")
    private @Getter @Setter Date createDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private @Getter @Setter List<Book> books = new ArrayList<>();

    @Override
    public String toString() { // TODO: replace with lombok or ToStringBuilder
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", avgRating=" + avgRating +
                ", createDate=" + createDate +
                '}';
    }
}
