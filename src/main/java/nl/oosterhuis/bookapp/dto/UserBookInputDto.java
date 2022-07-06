package nl.oosterhuis.bookapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserBookInputDto {
    @NotNull(message = "username cannot be null")
    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotNull(message = "favorite book id cannot be null")
    private Long favoriteBookId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getFavoriteBookId() {
        return favoriteBookId;
    }

    public void setFavoriteBookId(Long favoriteBookId) {
        this.favoriteBookId = favoriteBookId;
    }
}
