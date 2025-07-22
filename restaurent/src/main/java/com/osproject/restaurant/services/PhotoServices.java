package com.osproject.restaurant.services;

import com.osproject.restaurant.domain.entity.PhotoEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PhotoServices {

    PhotoEntity uploadPhoto(MultipartFile file);

    Optional<Resource> getPhotoAsResource(String id);
}