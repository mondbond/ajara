package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entity.listeners.CreateDateListener;
import entity.listeners.HasDate;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "book")
@EntityListeners({CreateDateListener.class})
@Table(name = "REVIEWS")
@Data
public class Review implements Serializable, HasDate {

    public static final String PK_COLUMN = "ID";
    public static final String NAME_COLUMN = "FIRST_NAME";
    public static final String AVG_RATING_COLUMN = "AVG_RATING";
    public static final String DATE_COLUMN = "CREATE_DATE";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_sequence")
    @SequenceGenerator(name = "review_id_sequence", allocationSize = 1, sequenceName = "REVIEW_PK_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMMENTER_NAME")
    @Pattern(regexp = "[a-zA-Z0-9- ]{3,100}", message = "Name must contain minimum 3 maximum 100 characters without special symbols")
    private String commenterName;

    @Column(name = "COMMENT_TEXT")
    private String commentText;

    @Column(name = "RATING")
    @Range(min = 0, max = 5, message = "Rating must be between 0 and 5")
    @NotNull
    private Integer rating;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_BOOK")
    private Book book;

    @Override
    public void setDate(LocalDateTime date) {
        createDate = date;
    }
}

