package com.example.filestorage.entities;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class FilesInfo extends BaseEntity {

    private String originalName;
    private long capacity;
    private String createdName;
    private String path;
}
