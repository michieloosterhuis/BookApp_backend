package nl.oosterhuis.bookapp.controller;

import nl.oosterhuis.bookapp.dto.BookDto;
import nl.oosterhuis.bookapp.dto.BookInputDto;
import nl.oosterhuis.bookapp.dto.BookUpdateDto;
import nl.oosterhuis.bookapp.exception.BookNotFoundException;
import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.InvalidRequestException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.FileUploadResponse;
import nl.oosterhuis.bookapp.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class BookController {

    private final BookService bookService;
    private final FileController fileController;

    public BookController(BookService bookService, FileController fileController) {
        this.bookService = bookService;
        this.fileController = fileController;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> getBooks(
            @RequestParam(value = "search", required = false, defaultValue = "") String searchText,
            @RequestParam(value = "sort", required = false, defaultValue = "title") String sortBy,
            @RequestParam(value = "languages", required = false, defaultValue = "dutch,english,german,french") String languages,
            @RequestParam(value = "transactionTypes", required = false, defaultValue = "gift,exchange_for_book,exchange_for_cake") String transactionTypes,
            @RequestParam(value = "isAvailable", required = false, defaultValue = "true") boolean isAvailable
    ) {
        List<BookDto> bookDtos = bookService.getBooks(searchText, languages, transactionTypes, sortBy, isAvailable);
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long bookId) throws BookNotFoundException {
        BookDto bookDto = bookService.getBook(bookId);
        return ResponseEntity.ok(bookDto);
    }

    @PostMapping("/books")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookInputDto bookInputDto) throws UserNotFoundException {
        BookDto bookDto = bookService.addBook(bookInputDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(bookDto);
    }

    @GetMapping("/available-books/{owner}")
    public ResponseEntity<List<BookDto>> getAvailableBooksFromUser(@PathVariable String owner) throws UserNotFoundException {
        List<BookDto> bookDtos = bookService.getAvailableBooksFromUser(owner);
        return ResponseEntity.ok(bookDtos);
    }

    @PatchMapping("/books/{bookId}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long bookId, @Valid @RequestBody BookUpdateDto bookUpdateDto) throws BookNotFoundException, InvalidRequestException {
        BookDto bookDto = bookService.updateBook(bookId, bookUpdateDto);
        return ResponseEntity.ok(bookDto);
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) throws BookNotFoundException, InvalidRequestException {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-books")
    public ResponseEntity<List<BookDto>> getMyBooks(Principal principal) throws UserNotFoundException {
        List<BookDto> bookDtos = bookService.getBooksFromUser(principal.getName());
        return ResponseEntity.ok(bookDtos);
    }

    @PostMapping("/my-books")
    public ResponseEntity<BookDto> addBook(Principal principal, @Valid @RequestBody BookInputDto bookInputDto) throws UserNotFoundException, InvalidRequestException {
        BookDto bookDto = bookService.addBookToUser(principal.getName(), bookInputDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(bookDto);
    }

    @PatchMapping("/my-books/{bookId}")
    public ResponseEntity<BookDto> updateMyBook(Principal principal, @PathVariable Long bookId, @Valid @RequestBody BookUpdateDto bookUpdateDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        BookDto bookDto = bookService.updateBookFromUser(principal.getName(), bookId, bookUpdateDto);
        return ResponseEntity.ok(bookDto);
    }

    @DeleteMapping("/my-books/{bookId}")
    public ResponseEntity<Void> deleteMyBook(Principal principal, @PathVariable Long bookId) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        bookService.deleteBookFromUser(principal.getName(), bookId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/my-books/{bookId}/book-cover")
    public ResponseEntity<BookDto> addBookCoverToMyBook(Principal principal, @PathVariable Long bookId, @Valid @RequestBody MultipartFile file) throws UserNotFoundException, FileNotFoundException, BookNotFoundException {
        FileUploadResponse fileUploadResponse = fileController.uploadSingleFile(file);
        return ResponseEntity.ok(bookService.addBookCoverToBookFromUser(principal.getName(), bookId, fileUploadResponse.getFileName()));
    }
}


