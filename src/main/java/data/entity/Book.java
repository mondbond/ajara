package data.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PUBLISHER")
    private String publisher;

    @Column(name = "PUBLISH_YEAR")
    private int publishYear;

    @Column(name = "CREATE_DATE")
    private Date crateDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "JOIN_BOOK_AUTHOR",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn (name= "author_id")})
    private Set<Author> authors = new HashSet<>();

    public Set<Author> getAuthors(){
      return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Book() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public Date getCrateDate() {
        return crateDate;
    }

    public void setCrateDate(Date crateDate) {
        this.crateDate = crateDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishYear=" + publishYear +
                ", crateDate=" + crateDate +
                ", authors=" + authors +
                '}';
    }
}
