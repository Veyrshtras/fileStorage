package com.example.filestorage.controllers;

import com.example.filestorage.dtos.FileDto;
import com.example.filestorage.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/s1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("upload")
    public ResponseEntity upload(@RequestParam("file")MultipartFile file){
        return ResponseEntity.status(HttpStatus.OK).body(fileService.uploadFile(file));
    }

    @GetMapping("download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename){
        return ResponseEntity.status(HttpStatus.OK).body(fileService.getByName(filename));
    }

    @GetMapping("getByCapacity")
    public ResponseEntity<List<FileDto>> getByCapacity(@RequestParam("from") long from, @RequestParam("to") long to){
        return ResponseEntity.status(HttpStatus.OK).body(fileService.getByCapacityInterval(from,to));
    }

    @GetMapping("getByDate")
    public ResponseEntity<List<FileDto>> getByDate(@RequestParam("from") Date from, @RequestParam("to") Date to){
        return ResponseEntity.status(HttpStatus.OK).body(fileService.getByDateInterval(from, to));
    }
    @GetMapping("getAll")
    public ResponseEntity<List<FileDto>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(fileService.findAll());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(FileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.notFound().build();
    }
}
