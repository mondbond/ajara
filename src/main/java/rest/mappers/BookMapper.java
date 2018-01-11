package rest.mappers;

import data.entity.Author;
import data.entity.Book;
import rest.dto.BookDto;

import java.util.function.Function;
import java.util.stream.Collectors;

public class BookMapper implements Function<Book,BookDto> {

    @Override
    public BookDto apply(Book book) {
        return new BookDto(book.getId(), book.getIsbn(), book.getName(), book.getPublisher(), // TODO: replace with builder
                book.getPublishYear(), book.getAvgRating(), book.getCrateDate(),
                book.getAuthors().stream()
                        .map(Author::getId)
                        .collect(Collectors.toList()));
    }
}
