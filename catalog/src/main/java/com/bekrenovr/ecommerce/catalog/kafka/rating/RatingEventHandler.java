package com.bekrenovr.ecommerce.catalog.kafka.rating;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingEventHandler {
    private final ItemRepository itemRepository;

    public void handle(RatingEvent event) {
        Item item = itemRepository.findByIdOrThrowDefault(event.itemId());
        item.setRating(event.averageRating());
        itemRepository.save(item);
    }
}
