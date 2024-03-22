package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemToDtoMapper;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.model.Item;
import com.bekrenovr.ecommerce.catalog.repository.LandingPageRepository;
import com.bekrenovr.ecommerce.catalog.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LandingPageService {
    private final LandingPageRepository landingPageRepository;
    private final ItemToDtoMapper itemToDtoMapper;

    @Value("${custom.page.size.landing-page}")
    private Integer pageSizeForLandingPage;

    public Page<ItemResponse> getLandingPageItems(Integer page) {
        List<Item> items = landingPageRepository.getLandingPageItems();
        return PageUtil.paginateList(items, page, pageSizeForLandingPage)
                .map(itemToDtoMapper::itemToResponse);
    }

    public void addLandingPageItems(List<Long> itemsIds){
        System.out.println(itemsIds);
        landingPageRepository.addLandingPageItems(itemsIds);
    }
}
