package nl.oosterhuis.bookapp.repository;

import nl.oosterhuis.bookapp.model.Book;
import nl.oosterhuis.bookapp.model.Transaction;
import nl.oosterhuis.bookapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByIdAndRequester(Long id, User requester);
    boolean existsByIdAndProvider(Long id, User provider);
    List<Transaction> findAllByRequesterOrProvider(User provider, User requester);
    List<Transaction> findAllByRequestedBookOrExchangeBook(Book requestBook, Book exchangeBook);
   }