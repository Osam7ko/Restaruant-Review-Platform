package com.osproject.restaurant.controller;

import com.osproject.restaurant.domain.dto.PhotoDto;
import com.osproject.restaurant.domain.entity.PhotoEntity;
import com.osproject.restaurant.mapper.PhotoMapper;
import com.osproject.restaurant.services.PhotoServices;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RestController("/api/photos")
public class PhotoController {

    private final PhotoServices photoServices;
    private final PhotoMapper photoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PhotoDto uploadPhoto(@RequestParam("file") MultipartFile file) {
        PhotoEntity savedPhoto = photoServices.uploadPhoto(file);
        return photoMapper.toDto(savedPhoto);
    }

    @GetMapping(path = "/{id:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String id) {
        return photoServices.getPhotoAsResource(id).map(photo ->
                ResponseEntity.ok()
                        .contentType(
                                MediaTypeFactory.getMediaType(photo).orElse(MediaType.APPLICATION_OCTET_STREAM)
                        )
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .body(photo)
        ).orElse(ResponseEntity.notFound().build());
    }

}