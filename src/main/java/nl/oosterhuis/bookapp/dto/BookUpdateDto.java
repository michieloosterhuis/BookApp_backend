package nl.oosterhuis.bookapp.dto;

import nl.oosterhuis.bookapp.model.Language;
import nl.oosterhuis.bookapp.model.TransactionType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BookUpdateDto {
    @NotNull(message = "title cannot be null")
    @NotBlank(message = "title cannot be empty")
    private String title;

    @NotNull(message = "author cannot be null")
    @NotBlank(message = "author cannot be empty")
    private String author;

    @NotNull(message = "year cannot be null")
    @Min(value = 1000, message = "year cannot be before 1000")
    private Long year;

    //TODO: check implementation of enum validation
    @NotNull(message = "language cannot be null")
    private Language language;

    @NotNull(message = "transaction type cannot be null")
    private TransactionType transactionType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
