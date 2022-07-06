package nl.oosterhuis.bookapp.dto;

import nl.oosterhuis.bookapp.model.FileUploadResponse;

public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private FileUploadResponse avatar;
    private String email;

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

    public FileUploadResponse getAvatar() {
        return avatar;
    }

    public void setAvatar(FileUploadResponse avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
