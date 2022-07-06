package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.UserBookDto;
import nl.oosterhuis.bookapp.dto.UserBookInputDto;
import nl.oosterhuis.bookapp.exception.*;

import java.util.List;

public interface UserBookService {
    List<UserBookDto> getAllFavorites();

    List<UserBookDto> getFavoritesFromUser(String username) throws UserNotFoundException;

    UserBookDto addFavorite(UserBookInputDto userBookInputDto) throws UserNotFoundException, BookNotFoundException, DuplicateFavoriteException;

    UserBookDto addFavoriteFromUser(String username, UserBookInputDto userBookInputDto) throws InvalidRequestException, UserNotFoundException, BookNotFoundException, DuplicateFavoriteException;

    void deleteFavorite(Long id) throws FavoriteNotFoundException;

    void deleteFavoriteFromUser(String username, Long favoriteId) throws UserNotFoundException, FavoriteNotFoundException, InvalidRequestException;
}
