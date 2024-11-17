package com.bekrenovr.ecommerce.catalog.item.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items/{id}/images")
@RequiredArgsConstructor
public class ItemImageController {
    private final ItemImageService itemImageService;

    @GetMapping
    public ResponseEntity<List<ItemImageResponse>> getAll(@PathVariable("id") UUID itemId){
        return ResponseEntity.ok(itemImageService.getAll(itemId));
    }

    @PostMapping
    public ResponseEntity<Void> upload(@PathVariable("id") UUID itemId, @RequestParam("images") List<MultipartFile> images) {
        itemImageService.upload(itemId, images);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public void delete(@PathVariable("id") UUID itemId, @RequestParam List<UUID> ids) {
        itemImageService.delete(itemId, ids);
    }
}
