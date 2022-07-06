package nl.oosterhuis.bookapp.exception;

public class FileNotReadableException extends Exception {
    public FileNotReadableException() {
    }

    public FileNotReadableException(String fileName) {
        super("File with file name '" + fileName + "' is not readable.");
    }
}
