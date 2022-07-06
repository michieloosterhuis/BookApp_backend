package nl.oosterhuis.bookapp.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String username) {
        super("User with username '" + username + "' does not exist.");
    }
}
