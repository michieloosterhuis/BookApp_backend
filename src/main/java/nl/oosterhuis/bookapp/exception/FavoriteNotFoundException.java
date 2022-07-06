package nl.oosterhuis.bookapp.exception;

public class FavoriteNotFoundException extends Exception {
    public FavoriteNotFoundException() {
    }

    public FavoriteNotFoundException(Long favoriteId) {
        super("Favorite with id '" + favoriteId + "' does not exist.");
    }
}
