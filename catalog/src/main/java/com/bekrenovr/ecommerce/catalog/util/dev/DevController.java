package com.bekrenovr.ecommerce.catalog.util.dev;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/dev/items")
@RequiredArgsConstructor
@Profile("dev")
public class DevController {
    private final ItemGenerator itemGenerator;
    private final ItemService itemService;
    private final ItemInsertGenerator itemInsertGenerator;

    @PostMapping("/{quantity}")
    public void createSampleItems(@PathVariable int quantity){
        for(int i = 0; i < quantity; i++){
            Item item = itemGenerator.generateItemWithDetails();
            itemService.create(item);
        }
    }

    @PostMapping("/inserts/{quantity}")
    public void createItemsInserts(@PathVariable int quantity) throws IOException {
        itemInsertGenerator.generateItemInserts(quantity);
    }
}
