package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.TransactionDto;
import nl.oosterhuis.bookapp.dto.TransactionInputDto;
import nl.oosterhuis.bookapp.dto.TransactionUpdateDto;
import nl.oosterhuis.bookapp.exception.BookNotFoundException;
import nl.oosterhuis.bookapp.exception.InvalidRequestException;
import nl.oosterhuis.bookapp.exception.TransactionNotFoundException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAllTransactions();

    List<TransactionDto> getAllTransactionsFromUser(String username) throws UserNotFoundException;

    TransactionDto getTransaction(Long transactionId) throws TransactionNotFoundException;

    TransactionDto getTransactionFromUser(String username, Long transactionId) throws UserNotFoundException, TransactionNotFoundException;

    TransactionDto addTransaction(TransactionInputDto transactionInputDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException;

    TransactionDto addTransactionToUser(String username, TransactionInputDto transactionInputDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException;

    TransactionDto assignExchangeBookToTransactionFromUser(String username, Long transactionId, TransactionUpdateDto transactionUpdateDto) throws TransactionNotFoundException, UserNotFoundException, InvalidRequestException, BookNotFoundException;

    TransactionDto approveTransactionFromUser(String username, Long transactionId) throws UserNotFoundException, TransactionNotFoundException, InvalidRequestException;

    void deleteTransaction(Long transactionId) throws TransactionNotFoundException;

    void deleteTransactionFromUser(String username, Long transactionId) throws UserNotFoundException, TransactionNotFoundException;

    void deleteAllTransactionsFromUser(String username) throws UserNotFoundException;
}
