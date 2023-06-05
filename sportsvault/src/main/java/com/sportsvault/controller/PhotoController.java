package com.sportsvault.controller;

import com.sportsvault.model.Photo;
import com.sportsvault.repository.PhotoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {
    private final PhotoRepository photoRepository;

    public PhotoController(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @PostMapping("/uploadPhotos")
    List<Photo> uploadPhotos(@RequestBody List<Photo> photoList) {
        return photoRepository.saveAll(photoList);
    }
}
