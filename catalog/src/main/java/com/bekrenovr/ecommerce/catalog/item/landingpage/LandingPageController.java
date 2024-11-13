package com.bekrenovr.ecommerce.catalog.item.landingpage;

import com.bekrenovr.ecommerce.catalog.item.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items/landing-page")
@RequiredArgsConstructor
public class LandingPageController {
    private final LandingPageService landingPageService;
    @GetMapping
    public ResponseEntity<Page<ItemResponse>> getLandingPageItems(
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "${custom.page.default-size}") Integer pageSize
    ){
        return ResponseEntity.ok(landingPageService.getLandingPageItems(pageNumber, pageSize));
    }

    @PostMapping
    public void addLandingPageItems(@RequestParam("ids")List<UUID> itemsIds){
        landingPageService.addLandingPageItems(itemsIds);
    }

    @DeleteMapping
    public void removeLandingPageItems(@RequestParam("ids") List<UUID> itemsIds){
        landingPageService.removeLandingPageItems(itemsIds);
    }
}
