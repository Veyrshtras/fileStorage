package com.example.filestorage.dtos;

import com.example.filestorage.entities.FilesInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class FileDto {
    private String originalName;
    private long capacity;
    private String createdName;
    private Date createdDate;

    public static  FileDto fromFileInfo(FilesInfo filesInfo){
        FileDto dto=new FileDto();
        dto.setOriginalName(filesInfo.getOriginalName());
        dto.setCapacity(filesInfo.getCapacity());
        dto.setCreatedName(filesInfo.getCreatedName());
        dto.setCreatedDate(filesInfo.getCreatedDate());
        return dto;
    };
}
