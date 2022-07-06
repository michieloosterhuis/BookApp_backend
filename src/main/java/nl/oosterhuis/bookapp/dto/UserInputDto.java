package nl.oosterhuis.bookapp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserInputDto {
    @NotNull(message = "username cannot be null")
    @NotBlank(message = "username cannot be empty")
    @Size(min = 4, max = 20, message = "username must be at least 4 and at most 20 characters long")
    private String username;

    @NotNull(message = "first name cannot be null")
    @NotBlank(message = "first name cannot be empty")
    private String firstName;

    @NotNull(message = "last name cannot be null")
    @NotBlank(message = "last name cannot be empty")
    private String lastName;

    @NotNull(message = "city cannot be null")
    @NotBlank(message = "city cannot be empty")
    private String city;

    @NotNull(message = "email cannot be null")
    @NotBlank(message = "email cannot be empty")
    @Email(message = "email address is not valid")
    private String email;

    @NotNull(message = "password cannot be null")
    @NotBlank(message = "password cannot be empty")
    @Size(min = 4, max = 20, message = "password must be at least 4 and at most 20 characters long")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
