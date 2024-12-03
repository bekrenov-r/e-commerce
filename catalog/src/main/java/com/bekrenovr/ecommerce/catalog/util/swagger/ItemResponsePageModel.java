package com.bekrenovr.ecommerce.catalog.util.swagger;

import com.bekrenovr.ecommerce.catalog.item.ItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class ItemResponsePageModel extends PageImpl<ItemResponse> {
    @Schema(implementation = ItemResponse.class)
    private List<ItemResponse> content;
    public ItemResponsePageModel(List<ItemResponse> content) {
        super(content);
    }
}
