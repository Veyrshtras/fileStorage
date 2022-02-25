package com.example.filestorage.services;

import com.example.filestorage.dtos.FileDto;
import com.example.filestorage.entities.FilesInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

public interface FileService {

    List<FileDto> findAll();

    List<FileDto> getByDateInterval(Date from, Date to);

    List<FileDto> getByCapacityInterval(long from, long to);

    Resource getByName(String filename);

    String uploadFile(MultipartFile file);
}
