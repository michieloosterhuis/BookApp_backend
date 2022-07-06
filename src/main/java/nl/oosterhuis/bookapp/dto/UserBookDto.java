package nl.oosterhuis.bookapp.dto;

public class UserBookDto {
    private Long id;
    private UserDto user;
    private BookDto favoriteBook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public BookDto getFavoriteBook() {
        return favoriteBook;
    }

    public void setFavoriteBook(BookDto favoriteBook) {
        this.favoriteBook = favoriteBook;
    }
}
