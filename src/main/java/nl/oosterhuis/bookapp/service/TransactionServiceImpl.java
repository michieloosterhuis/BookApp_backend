package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.TransactionDto;
import nl.oosterhuis.bookapp.dto.TransactionInputDto;
import nl.oosterhuis.bookapp.dto.TransactionUpdateDto;
import nl.oosterhuis.bookapp.exception.BookNotFoundException;
import nl.oosterhuis.bookapp.exception.InvalidRequestException;
import nl.oosterhuis.bookapp.exception.TransactionNotFoundException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.*;
import nl.oosterhuis.bookapp.repository.BookRepository;
import nl.oosterhuis.bookapp.repository.TransactionRepository;
import nl.oosterhuis.bookapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserServiceImpl userService;
    private final BookServiceImpl bookService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserServiceImpl userService, BookServiceImpl bookService, UserRepository userRepository, BookRepository bookRepository) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        return convertToTransactionDtos(transactionRepository.findAll());
    }

    @Override
    public List<TransactionDto> getAllTransactionsFromUser(String username) throws UserNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return convertToTransactionDtos(transactionRepository.findAllByRequesterOrProvider(user, user));
    }

    @Override
    public TransactionDto getTransaction(Long transactionId) throws TransactionNotFoundException {
        return convertToTransactionDto(transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId)));
    }

    @Override
    public TransactionDto getTransactionFromUser(String username, Long transactionId) throws UserNotFoundException, TransactionNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!transactionRepository.existsByIdAndRequester(transactionId, user)
                && !transactionRepository.existsByIdAndProvider(transactionId, user)) {
            throw new TransactionNotFoundException(transactionId);
        }
        return getTransaction(transactionId);
    }

    @Override
    public TransactionDto addTransaction(TransactionInputDto transactionInputDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        Transaction transaction = convertToTransaction(transactionInputDto);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToTransactionDto(savedTransaction);
    }

    @Override
    public TransactionDto addTransactionToUser(String username, TransactionInputDto transactionInputDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        if (!transactionInputDto.getRequesterUsername().equals(username))
            throw new InvalidRequestException("Current user must be the requester of the transaction.");
        return addTransaction(transactionInputDto);
    }

    @Override
    public TransactionDto assignExchangeBookToTransactionFromUser(String username, Long transactionId, TransactionUpdateDto transactionUpdateDto) throws TransactionNotFoundException, InvalidRequestException, BookNotFoundException, UserNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        final Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
        if (!transaction.getProvider().getUsername().equals(username))
            throw new InvalidRequestException("Current user must be the provider of the transaction.");
        if (!transaction.getTransactionType().equals(TransactionType.EXCHANGE_FOR_BOOK))
            throw new InvalidRequestException("Exchange books can only be added to transactions of type 'EXCHANGE_FOR_BOOK'.");
        if (!transaction.getTransactionStatusCode().equals(TransactionStatusCode.INITIALIZED))
            throw new InvalidRequestException("Exchange books can only be added to transactions with status 'INITIALIZED'.");
        if (transaction.getExchangeBook() != null)
            throw new InvalidRequestException("Transaction already has an exchange book.");

        final Book exchangeBook = bookRepository.findById(transactionUpdateDto.getExchangeBookId())
                .orElseThrow(() -> new BookNotFoundException(transactionUpdateDto.getExchangeBookId()));
        if (!exchangeBook.isAvailable())
            throw new InvalidRequestException("Exchange book is not available");

        transaction.setExchangeBook(exchangeBook);
        transaction.setTransactionStatusCode(TransactionStatusCode.EXCHANGE_BOOK_SELECTED);
        return convertToTransactionDto(transactionRepository.save(transaction));
    }

    @Override
    public TransactionDto approveTransactionFromUser(String username, Long transactionId) throws TransactionNotFoundException, InvalidRequestException, UserNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (transactionRepository.existsByIdAndRequester(transactionId, user)
                && transactionRepository.existsByIdAndProvider(transactionId, user))
            throw new TransactionNotFoundException(transactionId);

        final Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
        if (transaction.getTransactionType().equals(TransactionType.GIFT)
                && !transaction.getTransactionStatusCode().equals(TransactionStatusCode.INITIALIZED))
            throw new InvalidRequestException("Transactions of type 'GIFT' can only be approved when their status is equal to 'INITIALIZED'.");
        if (transaction.getTransactionType().equals(TransactionType.GIFT)
                && !transaction.getProvider().getUsername().equals(username))
            throw new InvalidRequestException("Transactions of type 'GIFT' can only be approved by the provider.");

        if (transaction.getTransactionType().equals(TransactionType.EXCHANGE_FOR_BOOK)
                && !transaction.getTransactionStatusCode().equals(TransactionStatusCode.EXCHANGE_BOOK_SELECTED))
            throw new InvalidRequestException("Transactions of type 'EXCHANGE_FOR_BOOK' can only be approved when their status is equal to 'EXCHANGE_BOOK_SELECTED'.");
        if (transaction.getTransactionType().equals(TransactionType.EXCHANGE_FOR_BOOK)
                && !transaction.getRequester().getUsername().equals(username))
            throw new InvalidRequestException("Transactions of type 'EXCHANGE_FOR_BOOK' can only be approved by the requester.");

        if (transaction.getTransactionType().equals(TransactionType.EXCHANGE_FOR_CAKE)
                && !transaction.getTransactionStatusCode().equals(TransactionStatusCode.INITIALIZED))
            throw new InvalidRequestException("Transactions of type 'EXCHANGE_FOR_CAKE' can only be approved when their status is equal to 'INITIALIZED'.");
        if (transaction.getTransactionType().equals(TransactionType.EXCHANGE_FOR_CAKE)
                && !transaction.getProvider().getUsername().equals(username))
            throw new InvalidRequestException("Transactions of type 'EXCHANGE_FOR_CAKE' can only be approved by the provider.");

        transaction.setTransactionStatusCode(TransactionStatusCode.FINALIZED);
        return convertToTransactionDto(transactionRepository.save(transaction));
    }

    @Override
    public void deleteTransaction(Long transactionId) throws TransactionNotFoundException {
        final Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
        transaction.getRequestedBook().setAvailable(true);
        Book exchangeBook = transaction.getExchangeBook();
        if (transaction.getExchangeBook() != null) {
            exchangeBook.setAvailable(true);
        }
        transactionRepository.delete(transaction);
    }

    @Override
    public void deleteTransactionFromUser(String username, Long transactionId) throws UserNotFoundException, TransactionNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!transactionRepository.existsByIdAndRequester(transactionId, user)
                && !transactionRepository.existsByIdAndProvider(transactionId, user)) {
            throw new TransactionNotFoundException(transactionId);
        }
        deleteTransaction(transactionId);
    }

    @Override
    public void deleteAllTransactionsFromUser(String username) throws UserNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        final List<Transaction> transactions = transactionRepository.findAllByRequesterOrProvider(user, user);
        transactionRepository.deleteAll(transactions);
    }


    public TransactionDto convertToTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = transaction.getDate();
        transactionDto.setDate(formatter.format(date));
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setTransactionStatusCode(transaction.getTransactionStatusCode());
        transactionDto.setRequester(userService.convertToUserDto(transaction.getRequester()));
        transactionDto.setProvider(userService.convertToUserDto(transaction.getProvider()));
        transactionDto.setRequestedBook(bookService.convertToBookDto(transaction.getRequestedBook()));
        if (transaction.getExchangeBook() != null)
            transactionDto.setExchangeBook(bookService.convertToBookDto(transaction.getExchangeBook()));
        return transactionDto;
    }

    public List<TransactionDto> convertToTransactionDtos(Collection<Transaction> transactions) {
        return transactions.stream().map(this::convertToTransactionDto).toList();
    }

    public Transaction convertToTransaction(TransactionInputDto transactionInputDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        User requester = userRepository.findById(transactionInputDto.getRequesterUsername())
                .orElseThrow(() -> new UserNotFoundException(transactionInputDto.getRequesterUsername()));

        Book requestedBook = bookRepository.findById(transactionInputDto.getRequestedBookId())
                .orElseThrow(() -> new BookNotFoundException(transactionInputDto.getRequestedBookId()));

        if (!requestedBook.isAvailable()) {
            throw new InvalidRequestException("Requested book is not available.");
        }
        if (requestedBook.getOwner().getUsername().equals(transactionInputDto.getRequesterUsername())) {
            throw new InvalidRequestException("Owners cannot request their own books.");
        }

        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setRequester(requester);
        transaction.setProvider(requestedBook.getOwner());
        transaction.setRequestedBook(requestedBook);
        transaction.setTransactionType(requestedBook.getTransactionType());
        transaction.setTransactionStatusCode(TransactionStatusCode.INITIALIZED);
        requestedBook.setAvailable(false);

        return transaction;
    }
}
