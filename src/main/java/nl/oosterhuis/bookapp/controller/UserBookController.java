package nl.oosterhuis.bookapp.controller;

import nl.oosterhuis.bookapp.dto.UserBookDto;
import nl.oosterhuis.bookapp.dto.UserBookInputDto;
import nl.oosterhuis.bookapp.exception.*;
import nl.oosterhuis.bookapp.service.UserBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class UserBookController {

    private final UserBookService userBookService;

    public UserBookController(UserBookService userBookService) {
        this.userBookService = userBookService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<UserBookDto>> getFavorites() {
        return ResponseEntity.ok(userBookService.getAllFavorites());
    }

    @PostMapping("/favorites")
    public ResponseEntity<UserBookDto> addFavorite(@Valid @RequestBody UserBookInputDto userBookInputDto) throws UserNotFoundException, BookNotFoundException, DuplicateFavoriteException {
        UserBookDto userBookDto = userBookService.addFavorite(userBookInputDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userBookDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(userBookDto);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long favoriteId) throws FavoriteNotFoundException {
        userBookService.deleteFavorite(favoriteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-favorites")
    public ResponseEntity<List<UserBookDto>> getMyFavorites(Principal principal) throws UserNotFoundException {
        List<UserBookDto> bookDtos = userBookService.getFavoritesFromUser(principal.getName());
        return ResponseEntity.ok(bookDtos);
    }

    @DeleteMapping("/my-favorites/{favoriteId}")
    public ResponseEntity<Void> deleteMyFavorite(Principal principal, @PathVariable Long favoriteId) throws UserNotFoundException, FavoriteNotFoundException, InvalidRequestException {
        userBookService.deleteFavoriteFromUser(principal.getName(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
