package nl.oosterhuis.bookapp.controller;

import nl.oosterhuis.bookapp.dto.TransactionDto;
import nl.oosterhuis.bookapp.dto.TransactionInputDto;
import nl.oosterhuis.bookapp.dto.TransactionUpdateDto;
import nl.oosterhuis.bookapp.exception.BookNotFoundException;
import nl.oosterhuis.bookapp.exception.InvalidRequestException;
import nl.oosterhuis.bookapp.exception.TransactionNotFoundException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.service.BookService;
import nl.oosterhuis.bookapp.service.TransactionService;
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
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService, BookService bookService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        List<TransactionDto> transactionDtos = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactionDtos);
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDto> addTransaction(@Valid @RequestBody TransactionInputDto transactionInputDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        TransactionDto transactionDto = transactionService.addTransaction(transactionInputDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(transactionDto);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long transactionId) throws TransactionNotFoundException {
        TransactionDto transactionDto = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(transactionDto);
    }

    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) throws TransactionNotFoundException {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-transactions")
    public ResponseEntity<List<TransactionDto>> getMyTransactions(Principal principal) throws UserNotFoundException {
        List<TransactionDto> transactionDtos = transactionService.getAllTransactionsFromUser(principal.getName());
        return ResponseEntity.ok(transactionDtos);
    }

    @PostMapping("/my-transactions")
    public ResponseEntity<TransactionDto> addTransaction(Principal principal, @Valid @RequestBody TransactionInputDto transactionInputDto) throws UserNotFoundException, BookNotFoundException, InvalidRequestException {
        TransactionDto transactionDto = transactionService.addTransactionToUser(principal.getName(), transactionInputDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(transactionDto);
    }

    @GetMapping("/my-transactions/{transactionId}")
    public ResponseEntity<TransactionDto> getMyTransaction(Principal principal, @PathVariable Long transactionId) throws UserNotFoundException, TransactionNotFoundException {
        TransactionDto transactionDto = transactionService.getTransactionFromUser(principal.getName(), transactionId);
        return ResponseEntity.ok(transactionDto);
    }

    @PatchMapping("/my-transactions/{transactionId}/exchange-book")
    public ResponseEntity<TransactionDto> assignExchangeBookToMyTransaction(Principal principal, @PathVariable Long transactionId, @Valid @RequestBody TransactionUpdateDto transactionUpdateDto) throws UserNotFoundException, BookNotFoundException, TransactionNotFoundException, InvalidRequestException {
        TransactionDto transactionDto = transactionService.assignExchangeBookToTransactionFromUser(principal.getName(), transactionId, transactionUpdateDto);
        return ResponseEntity.ok(transactionDto);
    }

    @PatchMapping("/my-transactions/{transactionId}/approve")
    public ResponseEntity<TransactionDto> approveMyTransaction(Principal principal, @PathVariable Long transactionId) throws UserNotFoundException, TransactionNotFoundException, InvalidRequestException {
        TransactionDto transactionDto = transactionService.approveTransactionFromUser(principal.getName(), transactionId);
        return ResponseEntity.ok(transactionDto);
    }

    @DeleteMapping("/my-transactions/{transactionId}")
    public ResponseEntity<Void> deleteMyTransaction(Principal principal, @PathVariable Long transactionId) throws UserNotFoundException, TransactionNotFoundException {
        transactionService.deleteTransactionFromUser(principal.getName(), transactionId);
        return ResponseEntity.noContent().build();
    }
}
