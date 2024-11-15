package com.bekrenovr.ecommerce.catalog.item.image;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.ItemRepository;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemImageService {
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final Cloudinary cloudinary;

    public List<ItemImageResponse> getAll(UUID itemId){
        List<ItemImage> itemImages = itemRepository.findByIdOrThrowDefault(itemId)
                .getImages();
        return itemImages.stream()
                .map(i -> new ItemImageResponse(i.getId(), i.getUrl()))
                .toList();
    }

    public void upload(UUID itemId, List<MultipartFile> images) {
        Item item = itemRepository.findByIdOrThrowDefault(itemId);
        List<String> urls = this.uploadToCloudinary(images);
        List<ItemImage> entities = urls.stream()
                .map(url -> new ItemImage(url, item))
                .toList();
        itemImageRepository.saveAll(entities);
    }

    private List<String> uploadToCloudinary(List<MultipartFile> images) {
        return images.stream()
                .map(image -> {
                    try {
                        Map<?, ?> response = cloudinary.uploader().upload(image.getBytes(), Collections.EMPTY_MAP);
                        return (String)response.get("url");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }
}
