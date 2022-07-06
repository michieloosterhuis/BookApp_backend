package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.*;
import nl.oosterhuis.bookapp.exception.BookNotFoundException;
import nl.oosterhuis.bookapp.exception.InvalidRequestException;
import nl.oosterhuis.bookapp.exception.TransactionNotFoundException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.*;
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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private BookServiceImpl bookService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User userA;
    private UserDto userDtoA;

    private User userB;
    private UserDto userDtoB;

    private Book bookA;
    private BookDto bookDtoA;

    private Book bookB;
    private BookDto bookDtoB;

    private Transaction transactionA;
    private Transaction updatedTransactionA;
    private TransactionDto transactionDtoA;
    private TransactionDto updatedTransactionDtoA;
    private TransactionInputDto transactionInputDtoA;
    private TransactionUpdateDto transactionUpdateDtoA;

    @BeforeEach
    public void setup() {
        userA = new User();
        userA.setUsername("christiaan");

        userDtoA = new UserDto();
        userDtoA.setUsername(userA.getUsername());

        userB = new User();
        userB.setUsername("michiel");

        userDtoB = new UserDto();
        userDtoB.setUsername(userB.getUsername());

        bookA = new Book();
        bookA.setId(1L);
        bookA.setOwner(userA);
        bookA.setAvailable(true);

        bookDtoA = new BookDto();
        bookDtoA.setId(bookA.getId());

        bookB = new Book();
        bookB.setId(2L);
        bookB.setOwner(userB);
        bookB.setAvailable(true);

        bookDtoB = new BookDto();
        bookDtoB.setId(bookB.getId());

        transactionA = new Transaction();
        transactionA.setId(1L);
        transactionA.setDate(new Date());
        transactionA.setTransactionType(TransactionType.EXCHANGE_FOR_BOOK);
        transactionA.setTransactionStatusCode(TransactionStatusCode.INITIALIZED);
        transactionA.setRequester(userA);
        transactionA.setProvider(userB);
        transactionA.setRequestedBook(bookB);
        transactionA.setExchangeBook(null);

        updatedTransactionA = new Transaction();
        updatedTransactionA.setId(transactionA.getId());
        updatedTransactionA.setDate(transactionA.getDate());
        updatedTransactionA.setTransactionType(transactionA.getTransactionType());
        updatedTransactionA.setTransactionStatusCode(TransactionStatusCode.EXCHANGE_BOOK_SELECTED);
        updatedTransactionA.setRequester(transactionA.getRequester());
        updatedTransactionA.setProvider(transactionA.getProvider());
        updatedTransactionA.setRequestedBook(transactionA.getRequestedBook());
        updatedTransactionA.setExchangeBook(bookA);

        transactionDtoA = new TransactionDto();
        transactionDtoA.setId(1L);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        transactionDtoA.setDate(formatter.format(transactionA.getDate()));
        transactionDtoA.setTransactionType(transactionA.getTransactionType());
        transactionDtoA.setTransactionStatusCode(transactionA.getTransactionStatusCode());
        transactionDtoA.setRequester(userDtoA);
        transactionDtoA.setProvider(userDtoB);
        transactionDtoA.setRequestedBook(bookDtoB);
        transactionDtoA.setExchangeBook(null);

        updatedTransactionDtoA = new TransactionDto();
        updatedTransactionDtoA.setId(transactionDtoA.getId());
        updatedTransactionDtoA.setDate(transactionDtoA.getDate());
        updatedTransactionDtoA.setTransactionType(transactionDtoA.getTransactionType());
        updatedTransactionDtoA.setTransactionStatusCode(TransactionStatusCode.EXCHANGE_BOOK_SELECTED);
        updatedTransactionDtoA.setRequester(transactionDtoA.getRequester());
        updatedTransactionDtoA.setProvider(transactionDtoA.getProvider());
        updatedTransactionDtoA.setRequestedBook(transactionDtoA.getRequestedBook());
        updatedTransactionDtoA.setExchangeBook(bookDtoA);

        transactionInputDtoA = new TransactionInputDto();
        transactionInputDtoA.setRequesterUsername(userA.getUsername());
        transactionInputDtoA.setRequestedBookId(bookB.getId());

        transactionUpdateDtoA = new TransactionUpdateDto();
        transactionUpdateDtoA.setExchangeBookId(bookA.getId());
    }

    @Test
    void givenTransactionList_whenGetAllTransactions_thenReturnTransactionDtoList() {
        // given
        given(transactionRepository.findAll()).willReturn(List.of(transactionA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(userService.convertToUserDto(userB)).willReturn(userDtoB);
        given(bookService.convertToBookDto(bookB)).willReturn(bookDtoB);
        // when
        List<TransactionDto> transactionDtos = transactionService.getAllTransactions();
        // then
        assertThat(transactionDtos).size().isEqualTo(1);
        assertThat(transactionDtos).element(0).usingRecursiveComparison().isEqualTo(transactionDtoA);
    }

    @Test
    void givenTransactionList_whenGetAllTransactionsFromUser_thenReturnTransactionDtoList() throws UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(transactionRepository.findAllByRequesterOrProvider(userA, userA)).willReturn(List.of(transactionA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(userService.convertToUserDto(userB)).willReturn(userDtoB);
        given(bookService.convertToBookDto(bookB)).willReturn(bookDtoB);
        // when
        List<TransactionDto> transactionDtos = transactionService.getAllTransactionsFromUser(userA.getUsername());
        // then
        assertThat(transactionDtos).size().isEqualTo(1);
        assertThat(transactionDtos).element(0).usingRecursiveComparison().isEqualTo(transactionDtoA);
    }

    @Test
    void givenTransactionIdAndTransaction_whenGetTransaction_thenReturnTransactionDto() throws TransactionNotFoundException {
        // given
        given(transactionRepository.findById(transactionA.getId())).willReturn(Optional.ofNullable(transactionA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(userService.convertToUserDto(userB)).willReturn(userDtoB);
        given(bookService.convertToBookDto(bookB)).willReturn(bookDtoB);
        // when
        TransactionDto transactionDto = transactionService.getTransaction(transactionA.getId());
        // then
        assertThat(transactionDto).usingRecursiveComparison().isEqualTo(transactionDtoA);
    }

    @Test
    void givenUsernameAndTransactionIdAndTransaction_whenGetTransactionFromUser_thenReturnTransactionDto() throws TransactionNotFoundException, UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(transactionRepository.existsByIdAndRequester(transactionA.getId(), userA)).willReturn(true);
        given(transactionRepository.existsByIdAndProvider(transactionA.getId(), userA)).willReturn(true);
        given(transactionRepository.findById(transactionA.getId())).willReturn(Optional.ofNullable(transactionA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(userService.convertToUserDto(userB)).willReturn(userDtoB);
        given(bookService.convertToBookDto(bookB)).willReturn(bookDtoB);
        // when
        TransactionDto transactionDto = transactionService.getTransactionFromUser(userA.getUsername(), transactionA.getId());
        // then
        assertThat(transactionDto).usingRecursiveComparison().isEqualTo(transactionDtoA);
    }

    @Test
    void givenTransactionInputDto_whenAddTransaction_thenReturnTransactionDto() throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(bookRepository.findById(bookB.getId())).willReturn(Optional.ofNullable(bookB));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(userService.convertToUserDto(userB)).willReturn(userDtoB);
        given(bookService.convertToBookDto(bookB)).willReturn(bookDtoB);
        given(transactionRepository.save(Mockito.any(Transaction.class))).willReturn(transactionA);
        // when
        TransactionDto transactionDto = transactionService.addTransaction(transactionInputDtoA);
        // then
        assertThat(transactionDto).usingRecursiveComparison().isEqualTo(transactionDtoA);
    }

    @Test
    void givenUsernameAndTransactionInputDto_whenAddTransactionFromUser_thenReturnTransactionDto() throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(bookRepository.findById(bookB.getId())).willReturn(Optional.ofNullable(bookB));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(userService.convertToUserDto(userB)).willReturn(userDtoB);
        given(bookService.convertToBookDto(bookB)).willReturn(bookDtoB);
        given(transactionRepository.save(Mockito.any(Transaction.class))).willReturn(transactionA);
        // when
        TransactionDto transactionDto = transactionService.addTransactionToUser(userA.getUsername(), transactionInputDtoA);
        // then
        assertThat(transactionDto).usingRecursiveComparison().isEqualTo(transactionDtoA);
    }

    @Test
    void givenUsernameAndTransactionUpdateDto_whenAssignExchangeBookToTransactionFromUser_thenReturnTransactionDto() throws UserNotFoundException, BookNotFoundException, TransactionNotFoundException, InvalidRequestException {
        // given
        given(userRepository.findById(userB.getUsername())).willReturn(Optional.ofNullable(userB));
        given(transactionRepository.findById(transactionA.getId())).willReturn(Optional.ofNullable(transactionA));
        given(bookRepository.findById(bookA.getId())).willReturn(Optional.ofNullable(bookA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(userService.convertToUserDto(userB)).willReturn(userDtoB);
        given(bookService.convertToBookDto(bookA)).willReturn(bookDtoA);
        given(bookService.convertToBookDto(bookB)).willReturn(bookDtoB);
        given(transactionRepository.save(Mockito.any(Transaction.class))).willReturn(updatedTransactionA);
        // when
        TransactionDto transactionDto = transactionService.assignExchangeBookToTransactionFromUser(userB.getUsername(), transactionA.getId(), transactionUpdateDtoA);
        // then
        assertThat(transactionDto).usingRecursiveComparison().isEqualTo(updatedTransactionDtoA);
    }

    @Test
    void givenTransactionId_whenDeleteTransaction_returnNothing() throws TransactionNotFoundException {
        // given
        given(transactionRepository.findById(transactionA.getId())).willReturn(Optional.ofNullable(transactionA));
        willDoNothing().given(transactionRepository).delete(transactionA);
        // when
        transactionService.deleteTransaction(transactionA.getId());
        // then
        verify(transactionRepository, times(1)).delete(transactionA);
    }

    @Test
    void givenUsernameAndTransactionId_whenDeleteTransactionFromUser_returnNothing() throws TransactionNotFoundException, UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(transactionRepository.existsByIdAndRequester(transactionA.getId(), userA)).willReturn(true);
        given(transactionRepository.existsByIdAndProvider(transactionA.getId(), userA)).willReturn(true);
        given(transactionRepository.findById(transactionA.getId())).willReturn(Optional.ofNullable(transactionA));
        willDoNothing().given(transactionRepository).delete(transactionA);
        // when
        transactionService.deleteTransactionFromUser(userA.getUsername(), transactionA.getId());
        // then
        verify(transactionRepository, times(1)).delete(transactionA);
    }

    @Test
    void givenUsername_whenDeleteAllTransactionsFromUser_thenReturnNothing() throws UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(transactionRepository.findAllByRequesterOrProvider(userA, userA)).willReturn(List.of(transactionA));
        willDoNothing().given(transactionRepository).deleteAll(List.of(transactionA));
        // when
        transactionService.deleteAllTransactionsFromUser(userA.getUsername());
        // then
        verify(transactionRepository, times(1)).deleteAll(List.of(transactionA));
    }
}