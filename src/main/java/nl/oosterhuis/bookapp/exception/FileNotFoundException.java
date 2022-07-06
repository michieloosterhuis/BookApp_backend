package nl.oosterhuis.bookapp.exception;

public class FileNotFoundException extends Exception {
    public FileNotFoundException() {
    }

    public FileNotFoundException(String fileName) {
        super("File with file name '" + fileName + "' does not exist.");
    }
}
