package rest.mappers;

import entity.Author;
import entity.Book;
import rest.dto.BookDto;

import java.util.function.Function;
import java.util.stream.Collectors;

public class BookMapper implements Function<Book,BookDto> {

    @Override
    public BookDto apply(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .name(book.getName())
                .publisher(book.getPublisher())
                .publishYear(book.getPublishYear())
                .averageRating(book.getAverageRating())
                .createDate(book.getCreateDate())
                .authors(book.getAuthors().stream()
                        .map(Author::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
