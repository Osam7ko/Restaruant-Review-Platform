package com.osproject.restaurant.services.impl;

import com.osproject.restaurant.exceptions.StorageException;
import com.osproject.restaurant.services.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    @Value("${app.storage.location:uploads}")
    private String storageLocation;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        rootLocation = Paths.get(storageLocation);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file, String filename) {
        // not empy
        if (file.isEmpty()) {
            throw new StorageException("Cannot save an empty file");
        }

        // size <= 5MB
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new StorageException("File size exceeds the maximum allowed size (5MB)");
        }

        // Path
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null) {
            throw new StorageException("File must have an extension");
        }

        // allowed extensions
        Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png", "pdf");
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new StorageException("File type not allowed");
        }

        // names
        // General name way
        String safeName = UUID.randomUUID().toString() + "." + extension.toLowerCase();

        // in the right directory
        Path destinationFile = rootLocation
                .resolve(Paths.get(safeName))
                .normalize()
                .toAbsolutePath();

        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside specified directory");
        }

        // safe save
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }

        return safeName;
    }

    @Override
    public Optional<Resource> loadResources(String filename) {

        try {
            Path file = rootLocation.resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return Optional.of(resource);
            } else {
                return Optional.empty();
            }
        } catch (MalformedURLException e) {
            log.warn("Cloud not read file: {}", filename, e);
            return Optional.empty();
        }

    }
}