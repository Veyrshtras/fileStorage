package com.example.filestorage.services.impl;
import com.example.filestorage.dtos.FileDto;
import com.example.filestorage.entities.FilesInfo;
import com.example.filestorage.repositories.FileRepository;
import com.example.filestorage.services.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository repository;

    public FileServiceImpl(FileRepository repository) {
        this.repository = repository;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class MyFileNotFoundException extends RuntimeException {
        public MyFileNotFoundException(String message) {
            super(message);
        }

        public MyFileNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    @Override
    public List<FileDto> findAll() {
        return repository.findAll()
                .stream()
                .map(FileDto::fromFileInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDto> getByDateInterval(Date from, Date to) {
        return repository.findAll()
                .stream()
                .filter(filesInfo -> filesInfo.getCreatedDate().compareTo(from)>=0
                        &&filesInfo.getCreatedDate().compareTo(to)<=0)
                .map(FileDto::fromFileInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDto> getByCapacityInterval(long from, long to) {
        return repository.findAll().stream()
                .filter(filesInfo -> filesInfo.getCapacity()>=from&&filesInfo.getCapacity()<=to)
                .map(FileDto::fromFileInfo)
                .collect(Collectors.toList());
    }

    @Override
    public Resource getByName(String filename) {
        List<FilesInfo> path= repository.findAll()
                .stream()
                .filter(filesInfo1 -> filesInfo1.getCreatedName().equals(filename))
                .collect(Collectors.toList());
        try {
            Path filePath = Paths.get(path.get(0).getPath() ).toAbsolutePath()
                    .normalize();
            try {
                Files.createDirectories(filePath);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }

            Path file=filePath.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + filename, ex);
        }

    }

    @Override
    public String uploadFile(MultipartFile file) {

        String originalName=file.getOriginalFilename();
        long capacity=file.getSize();
        Date createdDate= new Date();
        LocalDate date = LocalDate.ofInstant(createdDate.toInstant(), ZoneId.systemDefault());
        String createdName= FilenameUtils.getBaseName(originalName)+"_"+capacity+"_"+createdDate.getTime()+"."+ FilenameUtils.getExtension(originalName);
        String path="C:/Users/User/Desktop/uploads/"+date.getYear()+"/"+date.getMonth()+"/"+date.getDayOfMonth();


        FilesInfo filesInfo=new FilesInfo();
        filesInfo.setPath(path);
        filesInfo.setCapacity(capacity);
        filesInfo.setCreatedName(createdName);
        filesInfo.setOriginalName(originalName);
        filesInfo.setCreatedDate(createdDate);
        repository.save(filesInfo);
        try {

            new File(path).mkdir();
            File newFile = new File(path,createdName);
            FileUtils.writeByteArrayToFile(newFile, file.getBytes());
        }
        catch (IOException e){
            return (e.getMessage());
        }


        return createdName;
    }
}
