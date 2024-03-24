package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.model.Color;
import com.bekrenovr.ecommerce.catalog.model.ColorEnum;
import com.bekrenovr.ecommerce.catalog.model.Item;
import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.catalog.proxy.CustomerServiceProxy;
import com.bekrenovr.ecommerce.catalog.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.util.request.RequestScope;
import com.bekrenovr.ecommerce.catalog.util.request.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemMappingService {

    private final CustomerServiceProxy customerServiceProxy;
    private final ItemRepository itemRepository;
    private final RequestUtils requestUtils;

    @Value("${custom.strategy.popular-items-limit-in-scope}")
    private Integer limit;

    public List<ColorEnum> allColorsForItem(Item item){
        return item.getColors().stream()
                .map(Color::getValue)
                .toList();
    }

    public List<String> allSizesForItem(Item item){
        return item.getSizes().stream()
                .map(Size::getValue)
                .toList();
    }

    public boolean isItemOnWishList(UUID itemId){
        // todo: get customerId from security
        UUID customerId = null;
        return customerServiceProxy.isItemOnWishList(itemId, customerId);
    }

    public boolean isItemNew(Item item){
        LocalDate createdDate = item.getItemDetails().getCreatedAt().toLocalDate();
        return LocalDate.now().toEpochDay() - createdDate.toEpochDay() > 30;
    }

    public boolean isItemPopular(Item item){
        List<Item> popularItems = this.fetchPopularItemsInScope(requestUtils.getRequestScope(), item);
        return popularItems.contains(item);
    }

    public List<Item> fetchPopularItemsInScope(RequestScope requestScope, Item item){
        Pageable limitPageable = PageRequest.of(0, this.limit);
        return switch(requestScope){
            case GENDER_CATEGORY_AND_SUBCATEGORY ->
                    itemRepository.findPopularByGenderCategoryAndSubcategory(
                            item.getGender(), item.getCategory(), item.getSubcategory(), limitPageable);
            case GENDER_AND_CATEGORY ->
                    itemRepository.findPopularByGenderAndCategory(item.getGender(), item.getCategory(), limitPageable);
            case SEASON -> itemRepository.findPopularBySeason(item.getSeason(), limitPageable);
            case COLLECTION -> itemRepository.findPopularByCollection(item.getCollection(), limitPageable);
        };
    }
}
