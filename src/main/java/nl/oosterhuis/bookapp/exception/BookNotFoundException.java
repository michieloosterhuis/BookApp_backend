package nl.oosterhuis.bookapp.exception;

public class BookNotFoundException extends Exception {
    public BookNotFoundException() {
    }

    public BookNotFoundException(Long bookId) {
        super("Book with id '" + bookId + "' does not exist.");
    }
}
