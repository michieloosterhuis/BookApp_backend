package nl.oosterhuis.bookapp.dto;

import nl.oosterhuis.bookapp.model.TransactionStatusCode;
import nl.oosterhuis.bookapp.model.TransactionType;

public class TransactionDto {
    private Long id;
    private String date;
    private TransactionType transactionType;
    private TransactionStatusCode transactionStatusCode;
    private UserDto requester;
    private UserDto provider;
    private BookDto requestedBook;
    private BookDto exchangeBook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatusCode getTransactionStatusCode() {
        return transactionStatusCode;
    }

    public void setTransactionStatusCode(TransactionStatusCode transactionStatusCode) {
        this.transactionStatusCode = transactionStatusCode;
    }

    public UserDto getRequester() {
        return requester;
    }

    public void setRequester(UserDto requester) {
        this.requester = requester;
    }

    public UserDto getProvider() {
        return provider;
    }

    public void setProvider(UserDto provider) {
        this.provider = provider;
    }

    public BookDto getRequestedBook() {
        return requestedBook;
    }

    public void setRequestedBook(BookDto requestedBook) {
        this.requestedBook = requestedBook;
    }

    public BookDto getExchangeBook() {
        return exchangeBook;
    }

    public void setExchangeBook(BookDto exchangeBook) {
        this.exchangeBook = exchangeBook;
    }
}
