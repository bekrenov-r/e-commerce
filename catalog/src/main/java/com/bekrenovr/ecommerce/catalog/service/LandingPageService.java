package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemToDtoMapper;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.exception.ItemApplicationException;
import com.bekrenovr.ecommerce.catalog.model.Item;
import com.bekrenovr.ecommerce.catalog.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.repository.LandingPageRepository;
import com.bekrenovr.ecommerce.catalog.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

import static com.bekrenovr.ecommerce.catalog.exception.ItemApplicationExceptionReason.CANNOT_ADD_ITEMS_TO_LANDING_PAGE;
import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class LandingPageService {
    private final LandingPageRepository landingPageRepository;
    private final ItemToDtoMapper itemToDtoMapper;
    private final ItemRepository itemRepository;

    @Value("${custom.page.size.landing-page}")
    private Integer pageSizeForLandingPage;

    public Page<ItemResponse> getLandingPageItems(Integer page) {
        List<Item> items = landingPageRepository.getLandingPageItems();
        return PageUtil.paginateList(items, page, pageSizeForLandingPage)
                .map(itemToDtoMapper::itemToResponse);
    }

    public void addLandingPageItems(List<Long> itemsIds){
        Predicate<Item> isOnLandingPage = item -> landingPageRepository.getLandingPageItems().contains(item);
        List<Item> itemsToAdd = itemRepository.findAllById(itemsIds)
                .stream()
                .filter(not(isOnLandingPage))
                .toList();
        if(itemsToAdd.isEmpty()) throw new ItemApplicationException(CANNOT_ADD_ITEMS_TO_LANDING_PAGE);
        landingPageRepository.addLandingPageItems(itemsToAdd);
    }

    public void removeLandingPageItems(List<Long> itemsIds){
        List<Item> itemsToRemove = itemRepository.findAllById(itemsIds);
        landingPageRepository.removeLandingPageItems(itemsToRemove);
    }
}
