package nl.oosterhuis.bookapp.controller;

import nl.oosterhuis.bookapp.exception.FileNotFoundException;
import nl.oosterhuis.bookapp.exception.FileNotReadableException;
import nl.oosterhuis.bookapp.model.FileUploadResponse;
import nl.oosterhuis.bookapp.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    public FileUploadResponse uploadSingleFile(@RequestParam("file") MultipartFile file) {
        String url = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("api/v1/files/")
                .path(Objects.requireNonNull(file.getOriginalFilename()))
                .toUriString();
        String contentType = file.getContentType();
        String fileName = fileService.uploadFile(file, url);
        return new FileUploadResponse(fileName, contentType, url);
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName, HttpServletRequest httpServletRequest) throws FileNotReadableException, FileNotFoundException {
        Resource resource = fileService.downloadFile(fileName);
        String mimeType;

        try {
            mimeType = httpServletRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            throw new RuntimeException(e);
        }

//        for download as attachment use next line:
//        MediaType contentType = MediaType.IMAGE_GIF;
//        return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename()).body(resource);

//        for showing image in browser:
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }
}
