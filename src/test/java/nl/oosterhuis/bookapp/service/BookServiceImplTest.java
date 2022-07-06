package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.BookDto;
import nl.oosterhuis.bookapp.dto.BookInputDto;
import nl.oosterhuis.bookapp.dto.BookUpdateDto;
import nl.oosterhuis.bookapp.dto.UserDto;
import nl.oosterhuis.bookapp.exception.BookNotFoundException;
import nl.oosterhuis.bookapp.exception.InvalidRequestException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.Book;
import nl.oosterhuis.bookapp.model.Language;
import nl.oosterhuis.bookapp.model.TransactionType;
import nl.oosterhuis.bookapp.model.User;
import nl.oosterhuis.bookapp.repository.BookRepository;
import nl.oosterhuis.bookapp.repository.TransactionRepository;
import nl.oosterhuis.bookapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private User userA;
    private UserDto userDtoA;

    private Book bookA;
    private Book updatedBookA;
    private BookDto bookDtoA;
    private BookDto updatedBookDtoA;
    private BookInputDto bookInputDtoA;
    private BookUpdateDto bookUpdateDtoA;

    private Book bookB;
    private BookDto bookDtoB;

    @BeforeEach
    public void setup() {
        userA = new User();
        userA.setUsername("christiaan");

        userDtoA = new UserDto();
        userDtoA.setUsername("christiaan");

        bookA = new Book();
        bookA.setId(1L);
        bookA.setTitle("Book A");
        bookA.setAuthor("Author A");
        bookA.setYear(2000L);
        bookA.setLanguage(Language.DUTCH);
        bookA.setTransactionType(TransactionType.EXCHANGE_FOR_BOOK);
        bookA.setAvailable(true);
        bookA.setOwner(userA);

        updatedBookA = bookA;
        updatedBookA.setTitle("Book A updated");
        updatedBookA.setAuthor("Author A updated");
        updatedBookA.setYear(2020L);
        updatedBookA.setLanguage(Language.FRENCH);
        updatedBookA.setTransactionType(TransactionType.GIFT);

        bookDtoA = new BookDto();
        bookDtoA.setId(1L);
        bookDtoA.setTitle("Book A");
        bookDtoA.setAuthor("Author A");
        bookDtoA.setYear(2000L);
        bookDtoA.setLanguage(Language.DUTCH);
        bookDtoA.setTransactionType(TransactionType.EXCHANGE_FOR_BOOK);
        bookDtoA.setIsAvailable("true");
        bookDtoA.setOwner(userDtoA);

        updatedBookDtoA = bookDtoA;
        updatedBookDtoA.setTitle("Book A updated");
        updatedBookDtoA.setAuthor("Author A updated");
        updatedBookDtoA.setYear(2020L);
        updatedBookDtoA.setLanguage(Language.FRENCH);
        updatedBookDtoA.setTransactionType(TransactionType.GIFT);

        bookInputDtoA = new BookInputDto();
        bookInputDtoA.setTitle("Book A");
        bookInputDtoA.setAuthor("Author A");
        bookInputDtoA.setYear(2000L);
        bookInputDtoA.setLanguage(Language.DUTCH);
        bookInputDtoA.setTransactionType(TransactionType.EXCHANGE_FOR_BOOK);
        bookInputDtoA.setOwnerUsername(userA.getUsername());

        bookUpdateDtoA = new BookUpdateDto();
        bookUpdateDtoA.setTitle("Book A updated");
        bookUpdateDtoA.setAuthor("Author A updated");
        bookUpdateDtoA.setYear(2020L);
        bookUpdateDtoA.setLanguage(Language.FRENCH);
        bookUpdateDtoA.setTransactionType(TransactionType.GIFT);

        bookB = new Book();
        bookB.setId(2L);
        bookB.setTitle("Book B");
        bookB.setAuthor("Author B");
        bookB.setYear(2010L);
        bookB.setLanguage(Language.ENGLISH);
        bookB.setTransactionType(TransactionType.EXCHANGE_FOR_CAKE);
        bookB.setAvailable(false);
        bookB.setOwner(userA);

        bookDtoB = new BookDto();
        bookDtoB.setId(2L);
        bookDtoB.setTitle("Book B");
        bookDtoB.setAuthor("Author B");
        bookDtoB.setYear(2010L);
        bookDtoB.setLanguage(Language.ENGLISH);
        bookDtoB.setTransactionType(TransactionType.EXCHANGE_FOR_CAKE);
        bookDtoB.setIsAvailable("false");
        bookDtoB.setOwner(userDtoA);

        userA.setOwnedBooks(List.of(bookA, bookB));
    }

    @Test
    void givenBookList_whenGetBooks_thenReturnBookDtoList() {
        // given
        String searchText = "";
        String language = "dutch,english,german,french";
        String transactionType = "gift,exchange_for_book,exchange_for_cake";
        String sortBy = "title";
        boolean isAvailable = true;
        given(bookRepository.findByQuery(
                Mockito.anyList(),
                Mockito.anyList(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyBoolean())
        ).willReturn(List.of(bookA, bookB));
        when(userService.convertToUserDto(userA)).thenReturn(userDtoA);
        // when
        List<BookDto> bookDtos = bookService.getBooks(
                searchText,
                language,
                transactionType,
                sortBy,
                isAvailable
                );
        // then
        assertThat(bookDtos).size().isEqualTo(2);
        assertThat(bookDtos).element(0).usingRecursiveComparison().isEqualTo(bookDtoA);
        assertThat(bookDtos).element(1).usingRecursiveComparison().isEqualTo(bookDtoB);
    }

    @Test
    void givenOwnedBookList_whenGetOwnedBooksFromUser_thenReturnOwnedBookDtoList() throws UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        // when
        List<BookDto> bookDtos = bookService.getBooksFromUser(userA.getUsername());
        // then
        assertThat(bookDtos).size().isEqualTo(2);
        assertThat(bookDtos).element(0).usingRecursiveComparison().isEqualTo(bookDtoA);
        assertThat(bookDtos).element(1).usingRecursiveComparison().isEqualTo(bookDtoB);
    }

    @Test
    void givenOwnedBookList_whenGetAvailableBooksFromUser_thenReturnAvailableBookDtoList() throws UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        // when
        List<BookDto> bookDtos = bookService.getAvailableBooksFromUser(userA.getUsername());
        // then
        assertThat(bookDtos).size().isEqualTo(1);
        assertThat(bookDtos).element(0).usingRecursiveComparison().isEqualTo(bookDtoA);
    }

    @Test
    void givenBookId_whenGetBook_thenReturnBookDto() throws BookNotFoundException {
        // given
        given(bookRepository.findById(bookA.getId())).willReturn(Optional.ofNullable(bookA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        // when
        BookDto bookDto = bookService.getBook(bookA.getId());
        // then
        assertThat(bookDto).usingRecursiveComparison().isEqualTo(bookDtoA);
    }

    @Test
    void givenBookInputDto_whenAddBook_thenReturnBookDto() throws UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(bookRepository.save(Mockito.any(Book.class))).willReturn(bookA);
        // when
        BookDto bookDto = bookService.addBook(bookInputDtoA);
        // then
        assertThat(bookDto).usingRecursiveComparison().isEqualTo(bookDtoA);
    }

    @Test
    void givenUsernameAndBookInputDto_whenAddBook_thenReturnBookDto() throws UserNotFoundException, InvalidRequestException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(bookRepository.save(Mockito.any(Book.class))).willReturn(bookA);
        // when
        BookDto bookDto = bookService.addBookToUser(userA.getUsername(), bookInputDtoA);
        // then
        assertThat(bookDto).usingRecursiveComparison().isEqualTo(bookDtoA);
    }

    @Test
    void givenBookIdAndBookUpdateDto_whenUpdateBook_thenReturnUpdatedBookDto() throws BookNotFoundException, InvalidRequestException {
        // given
        given(bookRepository.findById(bookA.getId())).willReturn(Optional.ofNullable(bookA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(bookRepository.save(Mockito.any(Book.class))).willReturn(updatedBookA);
        // when
        BookDto bookDto = bookService.updateBook(bookA.getId(), bookUpdateDtoA);
        // then
        assertThat(bookDto).usingRecursiveComparison().isEqualTo(updatedBookDtoA);
    }

    @Test
    void givenUsernameAndBookIdAndBookUpdateDto_whenUpdateBook_thenReturnUpdatedBookDto() throws BookNotFoundException, InvalidRequestException, UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(bookRepository.existsByIdAndOwner(bookA.getId(), userA)).willReturn(true);
        given(bookRepository.findById(bookA.getId())).willReturn(Optional.ofNullable(bookA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(bookRepository.save(Mockito.any(Book.class))).willReturn(updatedBookA);
        // when
        BookDto bookDto = bookService.updateBookFromUser(userA.getUsername(), bookA.getId(), bookUpdateDtoA);
        // then
        assertThat(bookDto).usingRecursiveComparison().isEqualTo(updatedBookDtoA);
    }

    @Test
    void givenBookId_whenDeleteBook_thenReturnNothing() throws BookNotFoundException, InvalidRequestException {
        // given
        given(bookRepository.findById(bookA.getId())).willReturn(Optional.ofNullable(bookA));
        given(transactionRepository.findAllByRequestedBookOrExchangeBook(Mockito.any(), Mockito.any())).willReturn(List.of());
        willDoNothing().given(transactionRepository).deleteAll(Mockito.anyCollection());
        willDoNothing().given(bookRepository).delete(Mockito.any(Book.class));
        // when
        bookService.deleteBook(bookA.getId());
        // then
        verify(bookRepository, times(1)).delete(Mockito.any(Book.class));
    }

    @Test
    void givenUsernameAndBookId_whenDeleteBook_thenReturnNothing() throws BookNotFoundException, InvalidRequestException, UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(bookRepository.existsByIdAndOwner(bookA.getId(), userA)).willReturn(true);
        given(bookRepository.findById(bookA.getId())).willReturn(Optional.ofNullable(bookA));
        given(transactionRepository.findAllByRequestedBookOrExchangeBook(Mockito.any(), Mockito.any())).willReturn(List.of());
        willDoNothing().given(transactionRepository).deleteAll(Mockito.anyCollection());
        willDoNothing().given(bookRepository).delete(Mockito.any(Book.class));
        // when
        bookService.deleteBookFromUser(userA.getUsername(), bookA.getId());
        // then
        verify(bookRepository, times(1)).delete(Mockito.any(Book.class));
    }
}