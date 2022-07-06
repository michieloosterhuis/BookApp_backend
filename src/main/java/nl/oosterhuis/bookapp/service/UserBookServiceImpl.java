package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.UserBookDto;
import nl.oosterhuis.bookapp.dto.UserBookInputDto;
import nl.oosterhuis.bookapp.exception.*;
import nl.oosterhuis.bookapp.model.Book;
import nl.oosterhuis.bookapp.model.User;
import nl.oosterhuis.bookapp.model.UserBook;
import nl.oosterhuis.bookapp.repository.BookRepository;
import nl.oosterhuis.bookapp.repository.UserBookRepository;
import nl.oosterhuis.bookapp.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserBookServiceImpl implements UserBookService {

    private final UserBookRepository userBookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserServiceImpl userServiceImpl;
    private final BookServiceImpl bookServiceImpl;

    public UserBookServiceImpl(UserBookRepository userBookRepository, UserRepository userRepository, BookRepository bookRepository, UserServiceImpl userServiceImpl, BookServiceImpl bookServiceImpl) {
        this.userBookRepository = userBookRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userServiceImpl = userServiceImpl;
        this.bookServiceImpl = bookServiceImpl;
    }

    @Override
    public List<UserBookDto> getAllFavorites() {
        return convertToUserBookDtos(userBookRepository.findAll());
    }

    @Override
    public List<UserBookDto> getFavoritesFromUser(String username) throws UserNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return convertToUserBookDtos(userBookRepository.findAllByUser(user));
    }

    @Override
    public UserBookDto addFavorite(UserBookInputDto userBookInputDto) throws UserNotFoundException, BookNotFoundException, DuplicateFavoriteException {
        final UserBook userBook = convertToUserBook(userBookInputDto);
        if (userBookRepository.exists(Example.of(userBook)))
            throw new DuplicateFavoriteException(userBookInputDto.getUsername(), userBookInputDto.getFavoriteBookId());
        return convertToUserBookDto(userBookRepository.save(userBook));
    }

    @Override
    public UserBookDto addFavoriteFromUser(String username, UserBookInputDto userBookInputDto) throws InvalidRequestException, UserNotFoundException, BookNotFoundException, DuplicateFavoriteException {
        if (!userBookInputDto.getUsername().equals(username))
            throw new InvalidRequestException("Current user must be the liker of the book.");
        return addFavorite(userBookInputDto);
    }

    @Override
    public void deleteFavorite(Long favoriteId) throws FavoriteNotFoundException {
        if (!userBookRepository.existsById(favoriteId)) {
            throw new FavoriteNotFoundException(favoriteId);
        }
        userBookRepository.deleteById(favoriteId);
    }

    @Override
    public void deleteFavoriteFromUser(String username, Long favoriteId) throws UserNotFoundException, FavoriteNotFoundException, InvalidRequestException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!userBookRepository.existsByIdAndUser(favoriteId, user))
            throw new InvalidRequestException("User can only delete hit own favorites.");
        deleteFavorite(favoriteId);
    }


    public UserBookDto convertToUserBookDto(UserBook userBook) {
        UserBookDto userBookDto = new UserBookDto();
        userBookDto.setId(userBook.getId());
        userBookDto.setUser(userServiceImpl.convertToUserDto(userBook.getUser()));
        userBookDto.setFavoriteBook(bookServiceImpl.convertToBookDto(userBook.getBook()));
        return userBookDto;
    }

    public List<UserBookDto> convertToUserBookDtos(Collection<UserBook> userBooks) {
        return userBooks.stream().map(this::convertToUserBookDto).toList();
    }

    public UserBook convertToUserBook(UserBookInputDto userBookInputDto) throws UserNotFoundException, BookNotFoundException {
        UserBook userBook = new UserBook();
        User user = userRepository.findById(userBookInputDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userBookInputDto.getUsername()));
        userBook.setUser(user);
        Long favoriteBookId = userBookInputDto.getFavoriteBookId();
        Book favoriteBook = bookRepository.findById(favoriteBookId)
                .orElseThrow(() -> new BookNotFoundException(favoriteBookId));
        userBook.setBook(favoriteBook);
        return userBook;
    }
}
