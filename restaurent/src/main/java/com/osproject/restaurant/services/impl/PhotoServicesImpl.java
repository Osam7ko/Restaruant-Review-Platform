package com.osproject.restaurant.services.impl;

import com.osproject.restaurant.domain.entity.PhotoEntity;
import com.osproject.restaurant.services.PhotoServices;
import com.osproject.restaurant.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServicesImpl implements PhotoServices {

    private final StorageService storageService;

    @Override
    public PhotoEntity uploadPhoto(MultipartFile file) {
        String photoId = UUID.randomUUID().toString();
        String url = storageService.store(file, photoId);

        return PhotoEntity.builder()
                .url(url)
                .uploadDate(LocalDateTime.now())
                .build();
    }

    @Override
    public Optional<Resource> getPhotoAsResource(String id) {
        return storageService.loadResources(id);
    }
}