package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.FileNotReadableException;
import nl.oosterhuis.bookapp.model.FileUploadResponse;
import nl.oosterhuis.bookapp.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {
    @Value("${my.upload_location}")
    private Path fileStoragePath;
    private final String fileStorageLocation;
    private final FileUploadRepository fileUploadRepository;

    public FileServiceImpl(@Value("${my.upload_location}") String fileStorageLocation, FileUploadRepository fileUploadRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

        this.fileStorageLocation = fileStorageLocation;
        this.fileUploadRepository = fileUploadRepository;

        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue with creating file directory", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String url) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        //TODO: check slash type windows vs mac (windows: "\\")
        Path filePath = Paths.get(fileStoragePath + "/" + fileName);

        try {
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue with storing file", e);
        }

        fileUploadRepository.save(new FileUploadResponse(fileName, multipartFile.getContentType(), url));
        return fileName;
    }

    @Override
    public Resource downloadFile(String fileName) throws FileNotFoundException, FileNotReadableException {
        Path filePath = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;

        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue with reading the file", e);
        }

        if (!resource.exists()) {
            throw new FileNotFoundException(fileName);
        }
        if (!resource.isReadable()) {
            throw new FileNotReadableException(fileName);
        }
        return resource;
    }
}
