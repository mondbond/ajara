package data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @GeneratedValue(strategy=GenerationType.AUTO, generator="purchase_seq")
    @SequenceGenerator(name="purchase_seq", sequenceName="AJARA_SEQ")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "SECOND_NAME")
    private String secondName;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
