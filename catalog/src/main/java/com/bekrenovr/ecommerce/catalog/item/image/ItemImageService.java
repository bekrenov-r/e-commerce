package com.bekrenovr.ecommerce.catalog.item.image;

import com.bekrenovr.ecommerce.catalog.item.ItemRepository;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemImageService {
    private final ItemRepository itemRepository;
    private final ItemImageMapper itemImageMapper;

    public List<ItemImageResponse> getAllImagesForItem(UUID itemId){
        List<ItemImage> itemImages = itemRepository.findById(itemId)
                .orElseThrow(() -> new EcommerceApplicationException(ITEM_NOT_FOUND, itemId))
                .getImages();
        return itemImages.stream()
                .map(itemImageMapper::entityToResponse)
                .toList();
    }
}
