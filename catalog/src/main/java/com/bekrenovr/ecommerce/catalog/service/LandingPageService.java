package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemMapper;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemMetadata;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.jpa.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.jpa.repository.LandingPageRepository;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.CANNOT_ADD_ITEMS_TO_LANDING_PAGE;
import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class LandingPageService {
    private final LandingPageRepository landingPageRepository;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final ItemMetadataService itemMetadataService;


    public Page<ItemResponse> getLandingPageItems(Integer pageNumber, Integer pageSize) {
        List<Item> items = landingPageRepository.getLandingPageItems();
        Page<Item> paginatedItems = PageUtil.paginateList(items, pageNumber, pageSize);
        Map<Item, ItemMetadata> metadataMap = itemMetadataService.generateMetadata(paginatedItems);
        return paginatedItems
                .map(item -> itemMapper.itemToResponse(item, metadataMap.get(item)));
    }

    public void addLandingPageItems(List<UUID> itemsIds){
        Predicate<Item> isOnLandingPage = item -> landingPageRepository.getLandingPageItems().contains(item);
        List<Item> itemsToAdd = itemRepository.findAllById(itemsIds)
                .stream()
                .filter(not(isOnLandingPage))
                .toList();
        if(itemsToAdd.isEmpty()) throw new EcommerceApplicationException(CANNOT_ADD_ITEMS_TO_LANDING_PAGE);
        landingPageRepository.addLandingPageItems(itemsToAdd);
    }

    public void removeLandingPageItems(List<UUID> itemsIds){
        List<Item> itemsToRemove = itemRepository.findAllById(itemsIds);
        landingPageRepository.removeLandingPageItems(itemsToRemove);
    }
}
