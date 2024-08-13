package com.techlabs.app.service;

import com.techlabs.app.entity.FileItem;
import com.techlabs.app.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    private FileRepository repository;

    public FileServiceImpl(FileRepository repository) {
        this.repository = repository;
    }

    @Override
    public FileItem saveFile(FileItem file) {
        return repository.save(file);
    }

    @Override
    public Optional<FileItem> getFileByName(String fileName) {
        Optional<FileItem> item = repository.findByName(fileName);
        return item;
    }

	@Override
	public FileItem saveFile(MultipartFile file1) {
		return repository.save(file1);

	}
	@Override
	public FileItem saveFile2(MultipartFile file2) {
		return repository.save(file2);

	}


}