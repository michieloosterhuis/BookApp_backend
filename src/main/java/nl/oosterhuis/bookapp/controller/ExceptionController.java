package nl.oosterhuis.bookapp.controller;

import nl.oosterhuis.bookapp.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> handleException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateUserException.class)
    public ResponseEntity<Object> handleException(DuplicateUserException duplicateUserException) {
        return new ResponseEntity<>(duplicateUserException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BookNotFoundException.class)
    public ResponseEntity<Object> handleException(BookNotFoundException bookNotFoundException) {
        return new ResponseEntity<>(bookNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BookNotAvailableException.class)
    public ResponseEntity<Object> handleException(BookNotAvailableException bookNotAvailableException) {
        return new ResponseEntity<>(bookNotAvailableException.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = FavoriteNotFoundException.class)
    public ResponseEntity<Object> handleException(FavoriteNotFoundException favoriteNotFoundException) {
        return new ResponseEntity<>(favoriteNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateFavoriteException.class)
    public ResponseEntity<Object> handleException(DuplicateFavoriteException duplicateFavoriteException) {
        return new ResponseEntity<>(duplicateFavoriteException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = TransactionNotFoundException.class)
    public ResponseEntity<Object> handleException(TransactionNotFoundException transactionNotFoundException) {
        return new ResponseEntity<>(transactionNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<Object> handleException(FileNotFoundException fileNotFoundException) {
        return new ResponseEntity<>(fileNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<Object> handleException(InvalidRequestException invalidRequestException) {
        return new ResponseEntity<>(invalidRequestException.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}