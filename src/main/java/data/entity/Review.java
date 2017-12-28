package data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq")
    @SequenceGenerator(name="seq", allocationSize = 1, sequenceName="REVIEW_SEQ")
    private long id;

    @Column(name = "COMMENTER_NAME")
    private String commenterName;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "RATING")
    private int rating;

    @Column(name = "CREATE_DATE")
    private Date date;
}
