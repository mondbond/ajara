package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entity.listeners.CreateDateListener;
import entity.listeners.HasDate;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners({CreateDateListener.class})
@Table(name = "REVIEWS")
@Data
public class Reviews implements Serializable, HasDate {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="review_id_sequence")
    @SequenceGenerator(name="review_id_sequence", allocationSize = 1, sequenceName="REVIEW_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMMENTER_NAME")
    @Size(min = 3, max = 80, message = "Name must have minimum 3 maximum 80 characters")
    private String commenterName;

    @Column(name = "COM")
    private String com;

    @Column(name = "RATING")
    @Range(min = 0, max = 5, message = "Rating should be between 0 and 5")
    @NotNull
    private Integer rating;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_BOOK")
    private Book book;

    @Override
    public void setDate(Date date) {
        createDate = date;
    }
}

