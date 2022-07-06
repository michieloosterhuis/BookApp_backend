package nl.oosterhuis.bookapp.repository;

import nl.oosterhuis.bookapp.model.Book;
import nl.oosterhuis.bookapp.model.Language;
import nl.oosterhuis.bookapp.model.TransactionType;
import nl.oosterhuis.bookapp.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIdAndOwner(Long id, User owner);
    @Query(value = "SELECT book FROM Book book " +
            "WHERE " +
            "book.language IN :languages " +
            "AND " +
            "book.transactionType IN :transactionTypes " +
            "AND " +
            "(lower(book.title) LIKE lower(concat('%', :title, '%')) " +
            "OR " +
            "lower(book.author) LIKE lower(concat('%', :author, '%')))" +
            "AND " +
            "book.isAvailable = :isAvailable ")
    List<Book> findByQuery(List<Language> languages, List<TransactionType> transactionTypes, String title, String author, Sort sort, boolean isAvailable);
}