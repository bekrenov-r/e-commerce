package com.bekrenovr.ecommerce.catalog.item.image;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemImageService {
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final CloudinaryClient cloudinaryClient;

    public List<ItemImageResponse> getAll(UUID itemId){
        List<ItemImage> itemImages = itemRepository.findByIdOrThrowDefault(itemId)
                .getImages();
        return itemImages.stream()
                .map(i -> new ItemImageResponse(i.getId(), i.getUrl()))
                .toList();
    }

    @Transactional
    public void upload(UUID itemId, List<MultipartFile> images) {
        Item item = itemRepository.findByIdOrThrowDefault(itemId);
        List<String> urls = cloudinaryClient.uploadImages(images);
        List<ItemImage> entities = urls.stream()
                .map(url -> new ItemImage(url, item))
                .toList();
        itemImageRepository.saveAll(entities);
    }

    @Transactional
    public void delete(UUID itemId, List<UUID> ids) {
        Item item = itemRepository.findByIdOrThrowDefault(itemId);
        List<ItemImage> images = item.getImages()
                .stream()
                .filter(image -> ids.contains(image.getId()))
                .toList();
        List<String> urls = images.stream()
                .map(ItemImage::getUrl)
                .toList();
        cloudinaryClient.deleteImages(urls);
        itemImageRepository.deleteAll(images);
    }
}
