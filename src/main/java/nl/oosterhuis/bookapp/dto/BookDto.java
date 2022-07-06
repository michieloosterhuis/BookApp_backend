package nl.oosterhuis.bookapp.dto;

import nl.oosterhuis.bookapp.model.FileUploadResponse;
import nl.oosterhuis.bookapp.model.Language;
import nl.oosterhuis.bookapp.model.TransactionType;

public class BookDto {
    private Long id;
    private String title;
    private String author;
    private Long year;
    private Language language;
    private FileUploadResponse bookCover;
    private TransactionType transactionType;
    private String isAvailable;
    private UserDto owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public FileUploadResponse getBookCover() {
        return bookCover;
    }

    public void setBookCover(FileUploadResponse bookCover) {
        this.bookCover = bookCover;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }
}
