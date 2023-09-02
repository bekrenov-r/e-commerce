package com.ecommerce.itemsdata.dto.mapper;

import com.ecommerce.itemsdata.model.Color;
import com.ecommerce.itemsdata.model.ColorEnum;
import com.ecommerce.itemsdata.model.Item;
import com.ecommerce.itemsdata.model.Size;
import com.ecommerce.itemsdata.proxy.CustomerServiceProxy;
import com.ecommerce.itemsdata.repository.ItemRepository;
import com.ecommerce.itemsdata.util.request.RequestScope;
import com.ecommerce.itemsdata.util.request.RequestUtils;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
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

    public boolean isItemOnWishList(Long itemId){
        // todo: get customerId from security
        Long customerId = null;
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
            case AGE_GENDER_CATEGORY_AND_SUBCATEGORY ->
                    itemRepository.findPopularByAgeGenderCategoryAndSubcategory(
                            item.getAgeGroup(), item.getGender(), item.getCategory(), item.getSubcategory(), limitPageable);
            case GENDER_CATEGORY_AND_SUBCATEGORY ->
                    itemRepository.findPopularByGenderCategoryAndSubcategory(
                            item.getGender(), item.getCategory(), item.getSubcategory(), limitPageable);
            case AGE_GENDER_AND_CATEGORY ->
                    itemRepository.findPopularByAgeGenderAndCategory(
                            item.getAgeGroup(), item.getGender(), item.getCategory(), limitPageable);
            case GENDER_AND_CATEGORY ->
                    itemRepository.findPopularByGenderAndCategory(item.getGender(), item.getCategory(), limitPageable);
            case SEASON -> itemRepository.findPopularBySeason(item.getSeason(), limitPageable);
            case COLLECTION -> itemRepository.findPopularByCollection(item.getCollection(), limitPageable);
        };
    }
}
