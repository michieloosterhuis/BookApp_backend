package nl.oosterhuis.bookapp.repository;

import nl.oosterhuis.bookapp.model.FileUploadResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadResponse, String> {
}
