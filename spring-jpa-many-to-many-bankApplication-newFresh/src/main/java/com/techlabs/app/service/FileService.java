package com.techlabs.app.service;

import com.techlabs.app.entity.FileItem;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileItem saveFile(FileItem file);

    Optional<FileItem> getFileByName(String fileName);

	FileItem saveFile(MultipartFile file1);

	FileItem saveFile2(MultipartFile file2);
}