package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemMapper;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.jpa.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.jpa.repository.LandingPageRepository;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.util.PageUtil;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
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


    public Page<ItemResponse> getLandingPageItems(Integer pageNumber, Integer pageSize) {
        List<Item> items = landingPageRepository.getLandingPageItems();
        return PageUtil.paginateList(items, pageNumber, pageSize)
                .map(itemMapper::itemToResponse);
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
