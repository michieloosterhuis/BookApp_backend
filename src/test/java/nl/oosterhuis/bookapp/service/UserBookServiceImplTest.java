package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.BookDto;
import nl.oosterhuis.bookapp.dto.UserBookDto;
import nl.oosterhuis.bookapp.dto.UserBookInputDto;
import nl.oosterhuis.bookapp.dto.UserDto;
import nl.oosterhuis.bookapp.exception.*;
import nl.oosterhuis.bookapp.model.Book;
import nl.oosterhuis.bookapp.model.User;
import nl.oosterhuis.bookapp.model.UserBook;
import nl.oosterhuis.bookapp.repository.BookRepository;
import nl.oosterhuis.bookapp.repository.UserBookRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserBookServiceImplTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private BookServiceImpl bookService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserBookRepository userBookRepository;

    @InjectMocks
    private UserBookServiceImpl userBookService;

    private User userA;
    private UserDto userDtoA;

    private Book bookA;
    private BookDto bookDtoA;

    private UserBook userBookA;
    private UserBookDto userBookDtoA;
    private UserBookInputDto userBookInputDto;

    @BeforeEach
    public void setup() {
        userA = new User();
        userA.setUsername("christiaan");

        userDtoA = new UserDto();
        userDtoA.setUsername("christiaan");

        bookA = new Book();
        bookA.setId(1L);

        bookDtoA = new BookDto();
        bookDtoA.setId(1L);

        userBookA = new UserBook();
        userBookA.setId(1L);
        userBookA.setUser(userA);
        userBookA.setBook(bookA);

        userBookDtoA = new UserBookDto();
        userBookDtoA.setId(1L);
        userBookDtoA.setUser(userDtoA);
        userBookDtoA.setFavoriteBook(bookDtoA);

        userBookInputDto = new UserBookInputDto();
        userBookInputDto.setUsername("christiaan");
        userBookInputDto.setFavoriteBookId(1L);
    }

    @Test
    void givenFavoriteList_whenGetAllFavorites_thenReturnFavoriteDtoList() {
        // given
        given(userBookRepository.findAll()).willReturn(List.of(userBookA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(bookService.convertToBookDto(bookA)).willReturn(bookDtoA);
        // when
        List<UserBookDto> userBookDtos = userBookService.getAllFavorites();
        // then
        assertThat(userBookDtos).size().isEqualTo(1);
        assertThat(userBookDtos).element(0).usingRecursiveComparison().isEqualTo(userBookDtoA);
    }

    @Test
    void givenUsernameAndFavoriteList_whenGetAllFavorites_thenReturnFavoriteDtoList() throws UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(userBookRepository.findAllByUser(userA)).willReturn(List.of(userBookA));
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(bookService.convertToBookDto(bookA)).willReturn(bookDtoA);
        // when
        List<UserBookDto> userBookDtos = userBookService.getFavoritesFromUser(userA.getUsername());
        // then
        assertThat(userBookDtos).size().isEqualTo(1);
        assertThat(userBookDtos).element(0).usingRecursiveComparison().isEqualTo(userBookDtoA);
    }

    @Test
    void givenFavoriteInputDto_whenAddFavorite_thenReturnFavoriteDto() throws UserNotFoundException, BookNotFoundException, DuplicateFavoriteException {
        // given
        given(userRepository.findById(userBookInputDto.getUsername())).willReturn(Optional.ofNullable(userA));
        given(bookRepository.findById(userBookInputDto.getFavoriteBookId())).willReturn(Optional.ofNullable(bookA));
        given(userBookRepository.exists(Mockito.any())).willReturn(false);
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(bookService.convertToBookDto(bookA)).willReturn(bookDtoA);
        given(userBookRepository.save(Mockito.any(UserBook.class))).willReturn(userBookA);
        // when
        UserBookDto userBookDto = userBookService.addFavorite(userBookInputDto);
        // then
        assertThat(userBookDto).usingRecursiveComparison().isEqualTo(userBookDtoA);
    }

    @Test
    void givenUsernameAndFavoriteInputDto_whenAddFavorite_thenReturnFavoriteDto() throws UserNotFoundException, BookNotFoundException, DuplicateFavoriteException, InvalidRequestException {
        // given
        given(userRepository.findById(userBookInputDto.getUsername())).willReturn(Optional.ofNullable(userA));
        given(bookRepository.findById(userBookInputDto.getFavoriteBookId())).willReturn(Optional.ofNullable(bookA));
        given(userBookRepository.exists(Mockito.any())).willReturn(false);
        given(userService.convertToUserDto(userA)).willReturn(userDtoA);
        given(bookService.convertToBookDto(bookA)).willReturn(bookDtoA);
        given(userBookRepository.save(Mockito.any(UserBook.class))).willReturn(userBookA);
        // when
        UserBookDto userBookDto = userBookService.addFavoriteFromUser(userA.getUsername(), userBookInputDto);
        // then
        assertThat(userBookDto).usingRecursiveComparison().isEqualTo(userBookDtoA);
    }

    @Test
    void givenFavoriteId_whenDeleteFavoriet_thenReturnNothing() throws FavoriteNotFoundException {
        // given
        given(userBookRepository.existsById(userBookA.getId())).willReturn(true);
        willDoNothing().given(userBookRepository).deleteById(userBookA.getId());
        // when
        userBookService.deleteFavorite(userBookA.getId());
        // then
        verify(userBookRepository, times(1)).deleteById(userBookA.getId());
    }

    @Test
    void givenUsernameAndFavoriteId_whenDeleteFavoriet_thenReturnNothing() throws FavoriteNotFoundException, UserNotFoundException, InvalidRequestException {
        // given
        given(userRepository.findById(userA.getUsername())).willReturn(Optional.ofNullable(userA));
        given(userBookRepository.existsByIdAndUser(userBookA.getId(), userA)).willReturn(true);
        given(userBookRepository.existsById(userBookA.getId())).willReturn(true);
        willDoNothing().given(userBookRepository).deleteById(userBookA.getId());
        // when
        userBookService.deleteFavoriteFromUser(userA.getUsername(), userBookA.getId());
        // then
        verify(userBookRepository, times(1)).deleteById(userBookA.getId());
    }
}