package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.FileNotReadableException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile multipartFile, String url);

    Resource downloadFile(String fileName) throws FileNotFoundException, FileNotReadableException;
}
