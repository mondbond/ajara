package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REVIEWS")
@Data
public class Reviews implements Serializable {

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
    @NotNull
    private int rating;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_BOOK")
    private Book book;
}

