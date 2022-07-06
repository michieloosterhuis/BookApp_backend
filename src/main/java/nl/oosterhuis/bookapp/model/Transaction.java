package nl.oosterhuis.bookapp.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "transactions",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"requested_book_id", "exchange_book_id"})})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date")
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatusCode transactionStatusCode;

    @ManyToOne
    @JoinColumn(name = "requester_username")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "provider_username")
    private User provider;

    @OneToOne
    @JoinColumn(name = "requested_book_id")
    private Book requestedBook;

    @OneToOne
    @JoinColumn(name = "exchange_book_id")
    private Book exchangeBook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public Book getRequestedBook() {
        return requestedBook;
    }

    public void setRequestedBook(Book requestedBook) {
        this.requestedBook = requestedBook;
    }

    public Book getExchangeBook() {
        return exchangeBook;
    }

    public void setExchangeBook(Book exchangeBook) {
        this.exchangeBook = exchangeBook;
    }
}