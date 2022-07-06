package nl.oosterhuis.bookapp.controller;

import nl.oosterhuis.bookapp.dto.UserDto;
import nl.oosterhuis.bookapp.dto.UserInputDto;
import nl.oosterhuis.bookapp.dto.UserUpdateDto;
import nl.oosterhuis.bookapp.exception.DuplicateUserException;
import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.FileUploadResponse;
import nl.oosterhuis.bookapp.service.BookService;
import nl.oosterhuis.bookapp.service.TransactionService;
import nl.oosterhuis.bookapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final FileController fileController;

    public UserController(UserService userService, BookService bookService, TransactionService transactionService, FileController fileController) {
        this.userService = userService;
        this.fileController = fileController;
    }

    @GetMapping("/users")
    @Transactional
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/users/{username}")
    @Transactional
    public ResponseEntity<UserDto> getUser(@PathVariable String username) throws UserNotFoundException {
        UserDto userDto = userService.getUser(username);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserInputDto userInputDto) throws DuplicateUserException {
        UserDto userDto = userService.addUser(userInputDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(userDto.getUsername())
                .toUri();
        return ResponseEntity.created(location).body(userDto);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) throws UserNotFoundException {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-profile")
    @Transactional
    public ResponseEntity<UserDto> getMyProfile(Principal principal) throws UserNotFoundException {
        UserDto userDto = userService.getUser(principal.getName());
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/my-profile")
    public ResponseEntity<UserDto> updateMyProfile(Principal principal, @Valid @RequestBody UserUpdateDto userUpdateDto) throws UserNotFoundException, DuplicateUserException {
        return ResponseEntity.ok(userService.updateUser(principal.getName(), userUpdateDto));
    }

    @PatchMapping("/my-profile/avatar")
    public ResponseEntity<UserDto> addAvatarToMyProfile(Principal principal, @Valid @RequestBody MultipartFile file) throws UserNotFoundException, FileNotFoundException {
        FileUploadResponse fileUploadResponse = fileController.uploadSingleFile(file);
        return ResponseEntity.ok(userService.addAvatarToUser(principal.getName(), fileUploadResponse.getFileName()));
    }
}
