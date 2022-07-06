package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.BookDto;
import nl.oosterhuis.bookapp.dto.BookInputDto;
import nl.oosterhuis.bookapp.dto.BookUpdateDto;
import nl.oosterhuis.bookapp.exception.BookNotFoundException;
import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.InvalidRequestException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;

import java.util.List;

public interface BookService {

    List<BookDto> getBooks(String searchText, String language, String transactionType, String sortBy, boolean isAvailable);

    List<BookDto> getBooksFromUser(String username) throws UserNotFoundException;

    List<BookDto> getAvailableBooksFromUser(String username) throws UserNotFoundException;

    BookDto getBook(Long bookId) throws BookNotFoundException;

    BookDto addBook(BookInputDto bookInputDto) throws UserNotFoundException;

    BookDto addBookToUser(String username, BookInputDto bookInputDto) throws UserNotFoundException, InvalidRequestException;

    BookDto updateBook(Long bookId, BookUpdateDto bookUpdateDto) throws BookNotFoundException, InvalidRequestException;

    BookDto updateBookFromUser(String username, Long bookId, BookUpdateDto bookUpdateDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException;

    void deleteBook(Long bookId) throws BookNotFoundException, InvalidRequestException;

    void deleteBookFromUser(String username, Long bookId) throws UserNotFoundException, BookNotFoundException, InvalidRequestException;

    BookDto addBookCoverToBookFromUser(String username, Long bookId, String fileName) throws UserNotFoundException, BookNotFoundException, FileNotFoundException;
}
