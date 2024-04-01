package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemImageMapper;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemImageResponse;
import com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationException;
import com.bekrenovr.ecommerce.catalog.jpa.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.model.entity.ItemImage;
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
                .orElseThrow(() -> new CatalogApplicationException(ITEM_NOT_FOUND, itemId))
                .getImages();
        return itemImages.stream()
                .map(itemImageMapper::entityToResponse)
                .toList();
    }
}
