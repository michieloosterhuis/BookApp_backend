package nl.oosterhuis.bookapp.exception;

public class BookNotAvailableException extends Exception {
    public BookNotAvailableException() {
    }

    public BookNotAvailableException(Long bookId) {
        super("Book with id '" + bookId + "' is not available.");
    }
}
