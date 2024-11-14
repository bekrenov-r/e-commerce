package com.bekrenovr.ecommerce.catalog.item.image;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.ItemRepository;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemImageService {
    private final ItemRepository itemRepository;

    public List<ItemImageResponse> getAll(UUID itemId){
        List<ItemImage> itemImages = itemRepository.findById(itemId)
                .orElseThrow(() -> new EcommerceApplicationException(ITEM_NOT_FOUND, itemId))
                .getImages();
        return itemImages.stream()
                .map(i -> new ItemImageResponse(i.getId(), i.getUrl()))
                .toList();
    }

    public void upload(UUID itemId, List<MultipartFile> images) {
        Item item = itemRepository.findByIdOrThrowDefault(itemId);
    }
}
