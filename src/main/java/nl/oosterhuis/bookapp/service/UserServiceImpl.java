package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.UserDto;
import nl.oosterhuis.bookapp.dto.UserInputDto;
import nl.oosterhuis.bookapp.dto.UserUpdateDto;
import nl.oosterhuis.bookapp.exception.DuplicateUserException;
import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.Authority;
import nl.oosterhuis.bookapp.model.FileUploadResponse;
import nl.oosterhuis.bookapp.model.Transaction;
import nl.oosterhuis.bookapp.model.User;
import nl.oosterhuis.bookapp.repository.FileUploadRepository;
import nl.oosterhuis.bookapp.repository.TransactionRepository;
import nl.oosterhuis.bookapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final FileUploadRepository fileUploadRepository;

    public UserServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository, FileUploadRepository fileUploadRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.fileUploadRepository = fileUploadRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return convertToUserDtos(userRepository.findAll());
    }

    @Override
    public UserDto getUser(String username) throws UserNotFoundException {
        return convertToUserDto(userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username)));
    }

    @Override
    public UserDto addUser(UserInputDto userInputDto) throws DuplicateUserException {
        if (userRepository.existsByUsername(userInputDto.getUsername())) {
            throw new DuplicateUserException("User with username '" + userInputDto.getUsername() + "' already exists.");
        }
        if (userRepository.existsByEmail(userInputDto.getEmail())) {
            throw new DuplicateUserException("User with email '" + userInputDto.getEmail() + "' already exists.");
        }
        return convertToUserDto(userRepository.save(convertToUser(userInputDto)));
    }

    @Override
    public UserDto updateUser(String username, UserUpdateDto userUpdateDto) throws UserNotFoundException, DuplicateUserException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (userUpdateDto.getFirstName() != null) user.setFirstName(userUpdateDto.getFirstName());
        if (userUpdateDto.getLastName() != null) user.setLastName(userUpdateDto.getLastName());
        if (userUpdateDto.getCity() != null) user.setCity(userUpdateDto.getCity());
        if (userUpdateDto.getEmail() != null) {
            if (userRepository.existsByEmail(userUpdateDto.getEmail()) && !userUpdateDto.getEmail().equals(user.getEmail())) {
                throw new DuplicateUserException("User with email '" + userUpdateDto.getEmail() + "' already exists.");
            } else {
                user.setEmail(userUpdateDto.getEmail());
            }
        }
        return convertToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto addAvatarToUser(String username, String fileName) throws UserNotFoundException, FileNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        final FileUploadResponse fileUploadResponse = fileUploadRepository.findById(fileName)
                .orElseThrow(() -> new FileNotFoundException(fileName));
        user.setAvatar(fileUploadResponse);
        return convertToUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(String username) throws UserNotFoundException {
        final User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        final List<Transaction> transactions = transactionRepository.findAllByRequesterOrProvider(user, user);
        transactionRepository.deleteAll(transactions);
        userRepository.delete(user);
    }


    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setCity(user.getCity());
        userDto.setEmail(user.getEmail());
        userDto.setAvatar(user.getAvatar());
        return userDto;
    }

    public List<UserDto> convertToUserDtos(Collection<User> users) {
        return users.stream().map(this::convertToUserDto).toList();
    }

    public User convertToUser(UserInputDto userInputDto) {
        User user = new User();
        user.setUsername(userInputDto.getUsername());
        user.setFirstName(userInputDto.getFirstName());
        user.setLastName(userInputDto.getLastName());
        user.setCity(userInputDto.getCity());
        user.setEmail(userInputDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userInputDto.getPassword()));
        user.setEnabled(true);
        user.getAuthorities().add(new Authority(user.getUsername(), "ROLE_USER"));
        return user;
    }
}
