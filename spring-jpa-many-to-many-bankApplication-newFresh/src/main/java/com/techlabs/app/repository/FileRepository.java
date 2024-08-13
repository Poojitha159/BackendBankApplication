package com.techlabs.app.repository;

import com.techlabs.app.entity.FileItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileItem, Integer> {
    Optional<FileItem> findByName(String fileName);

	FileItem save(MultipartFile file1);
}