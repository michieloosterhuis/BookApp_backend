package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.UserDto;
import nl.oosterhuis.bookapp.dto.UserInputDto;
import nl.oosterhuis.bookapp.dto.UserUpdateDto;
import nl.oosterhuis.bookapp.exception.DuplicateUserException;
import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUser(String username) throws UserNotFoundException;

    UserDto addUser(UserInputDto userInputDto) throws DuplicateUserException;

    UserDto updateUser(String username, UserUpdateDto userUpdateDto) throws UserNotFoundException, DuplicateUserException;

    UserDto addAvatarToUser(String username, String fileName) throws UserNotFoundException, FileNotFoundException;

    void deleteUser(String username) throws UserNotFoundException;
}
