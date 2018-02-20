package rest.mappers;

import entity.Author;
import entity.Book;
import rest.dto.AuthorDto;

import java.util.function.Function;
import java.util.stream.Collectors;

public class AuthorMapper implements Function<Author,AuthorDto> {
    @Override
    public AuthorDto apply(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .secondName(author.getSecondName())
                .averageRating(author.getAverageRating())
                .createDate(author.getCreateDate())
                .books(author.getBooks().stream()
                        .map(Book::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
