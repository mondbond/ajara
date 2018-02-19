package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "book", callSuper = true)
@SequenceGenerator(name = "variable_sequence", allocationSize = 1, sequenceName = "REVIEW_PK_SEQ")
@Table(name = "REVIEWS")
@Data
public class Review extends BaseEntity {

    public static final String PK_COLUMN = "ID";
    public static final String NAME_COLUMN = "FIRST_NAME";
    public static final String AVG_RATING_COLUMN = "AVG_RATING";
    public static final String DATE_COLUMN = "CREATE_DATE";

    @Column(name = "COMMENTER_NAME")
    @Pattern(regexp = "[a-zA-Z0-9- ]{3,100}", message = "Name must contain minimum 3 maximum 100 characters without special symbols")
    private String commenterName;

    @Column(name = "COMMENT_TEXT")
    private String commentText;

    @Column(name = "RATING")
    @Range(min = 0, max = 5, message = "Rating must be between 0 and 5")
    @NotNull
    private Integer rating;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_BOOK")
    private Book book;
}

