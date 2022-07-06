package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.*;
import nl.oosterhuis.bookapp.exception.BookNotFoundException;
import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.InvalidRequestException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.*;
import nl.oosterhuis.bookapp.repository.BookRepository;
import nl.oosterhuis.bookapp.repository.FileUploadRepository;
import nl.oosterhuis.bookapp.repository.TransactionRepository;
import nl.oosterhuis.bookapp.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final FileUploadRepository fileUploadRepository;

    public BookServiceImpl(BookRepository bookRepository, UserServiceImpl userService, UserRepository userRepository, TransactionRepository transactionRepository, FileUploadRepository fileUploadRepository) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.fileUploadRepository = fileUploadRepository;
    }

    @Override
    public List<BookDto> getBooks(String searchText, String language, String transactionType, String sortBy, boolean isAvailable) {

        String search = searchText.split(",")[0];

        Set<Language> languages = null;
        if (language == null) {
            languages = Arrays.stream(Language.values()).collect(Collectors.toSet());
        } else {
            languages = Arrays.asList(language.split(",")).stream()
                    .map(String::toUpperCase)
                    .filter(Language::isValid)
                    .map(Language::valueOf)
                    .collect(Collectors.toSet());
        }

        Set<TransactionType> transactionTypes = null;
        if (transactionType == null) {
            transactionTypes = Arrays.stream(TransactionType.values()).collect(Collectors.toSet());
        } else {
            transactionTypes = Arrays.asList(transactionType.split(",")).stream()
                    .map(String::toUpperCase)
                    .filter(TransactionType::isValid)
                    .map(TransactionType::valueOf)
                    .collect(Collectors.toSet());
        }

        Sort sort;
        sortBy = sortBy.split(",")[0].toLowerCase();
        if (sortBy.equals("title") || sortBy.equals("author") || sortBy.equals("year")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by("title").ascending();
        }

        return convertToBookDtos(bookRepository.findByQuery(
                new ArrayList<>(languages),
                new ArrayList<>(transactionTypes),
                search,
                search,
                sort,
                isAvailable));
    }

    @Override
    public List<BookDto> getBooksFromUser(String username) throws UserNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return convertToBookDtos(user.getOwnedBooks());
    }

    @Override
    public List<BookDto> getAvailableBooksFromUser(String username) throws UserNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        final List<Book> ownedBooks = user.getOwnedBooks();
        final List<Book> availableBooks = ownedBooks.stream().filter(Book::isAvailable).toList();
        return convertToBookDtos(availableBooks);
    }

    @Override
    public BookDto getBook(Long bookId) throws BookNotFoundException {
        return convertToBookDto(bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId)));
    }

    @Override
    public BookDto addBook(BookInputDto bookInputDto) throws UserNotFoundException {
        Book book = convertToBook(bookInputDto);
        Book savedBook = bookRepository.save(book);
        return convertToBookDto(savedBook);
    }

    @Override
    public BookDto addBookToUser(String username, BookInputDto bookInputDto) throws UserNotFoundException, InvalidRequestException {
        if (!bookInputDto.getOwnerUsername().equals(username))
            throw new InvalidRequestException("Current user must be the owner of the book.");
        return addBook(bookInputDto);
    }

    @Override
    public BookDto updateBook(Long bookId, BookUpdateDto bookUpdateDto) throws BookNotFoundException, InvalidRequestException {
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        if (!book.isAvailable())
            throw new InvalidRequestException("Only available books can be modified.");
        if (bookUpdateDto.getTitle() != null)
            book.setTitle(bookUpdateDto.getTitle());
        if (bookUpdateDto.getAuthor() != null)
            book.setAuthor(bookUpdateDto.getAuthor());
        if (bookUpdateDto.getYear() != null)
            book.setYear(bookUpdateDto.getYear());
        if (bookUpdateDto.getLanguage() != null)
            book.setLanguage(bookUpdateDto.getLanguage());
        if (bookUpdateDto.getTransactionType() != null)
            book.setTransactionType(bookUpdateDto.getTransactionType());
        return convertToBookDto(bookRepository.save(book));
    }

    @Override
    public BookDto updateBookFromUser(String username, Long bookId, BookUpdateDto bookUpdateDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!bookRepository.existsByIdAndOwner(bookId, user))
            throw new InvalidRequestException("Only owners can modify books.");
        return updateBook(bookId, bookUpdateDto);
    }

    @Override
    public void deleteBook(Long bookId) throws BookNotFoundException, InvalidRequestException {
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        if (!book.isAvailable())
            throw new InvalidRequestException("Only available books can be deleted.");
        final List<Transaction> transactions = transactionRepository.findAllByRequestedBookOrExchangeBook(book, book);
        transactionRepository.deleteAll(transactions);
        bookRepository.delete(book);
    }

    @Override
    public void deleteBookFromUser(String username, Long bookId) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!bookRepository.existsByIdAndOwner(bookId, user))
            throw new InvalidRequestException("Only the owner can delete a book.");
        deleteBook(bookId);
    }

    @Override
    public BookDto addBookCoverToBookFromUser(String username, Long bookId, String fileName) throws UserNotFoundException, BookNotFoundException, FileNotFoundException {
        if (!userRepository.existsById(username))
            throw new UserNotFoundException(username);
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        final FileUploadResponse fileUploadResponse = fileUploadRepository.findById(fileName)
                .orElseThrow(() -> new FileNotFoundException(fileName));
        book.setBookCover(fileUploadResponse);
        return convertToBookDto(bookRepository.save(book));
    }


    public BookDto convertToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setYear(book.getYear());
        bookDto.setLanguage(book.getLanguage());
        bookDto.setBookCover(book.getBookCover());
        bookDto.setTransactionType(book.getTransactionType());
        bookDto.setIsAvailable(String.valueOf(book.isAvailable()));
        bookDto.setOwner(userService.convertToUserDto(book.getOwner()));
        return bookDto;
    }

    public List<BookDto> convertToBookDtos(Collection<Book> books) {
        return books.stream().map(this::convertToBookDto).toList();
    }

    public Book convertToBook(BookInputDto bookInputDto) throws UserNotFoundException {
        final User owner = userRepository.findById(bookInputDto.getOwnerUsername())
                .orElseThrow(() -> new UserNotFoundException(bookInputDto.getOwnerUsername()));
        Book book = new Book();
        book.setOwner(owner);
        book.setTitle(bookInputDto.getTitle());
        book.setAuthor(bookInputDto.getAuthor());
        book.setYear(bookInputDto.getYear());
        book.setLanguage(bookInputDto.getLanguage());
        book.setTransactionType(bookInputDto.getTransactionType());
        book.setAvailable(true);
        return book;
    }
}
