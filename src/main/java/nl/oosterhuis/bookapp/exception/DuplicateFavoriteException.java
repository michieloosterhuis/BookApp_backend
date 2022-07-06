package nl.oosterhuis.bookapp.exception;

public class DuplicateFavoriteException extends Exception {
    public DuplicateFavoriteException() {
    }

    public DuplicateFavoriteException(String favoriteId) {
        super("Favorite with id '" + favoriteId + "' already exists.");
    }

    public DuplicateFavoriteException(String username, Long favoriteBookId) {
        super("User with username '" + username + "' has already a favorite book with id '" + favoriteBookId + "'.");
    }
}
