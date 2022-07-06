package nl.oosterhuis.bookapp.repository;

import nl.oosterhuis.bookapp.model.User;
import nl.oosterhuis.bookapp.model.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    boolean existsByIdAndUser(Long id, User user);
    List<UserBook> findAllByUser(User user);
}