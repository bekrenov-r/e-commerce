package com.bekrenovr.ecommerce.catalog.item.landingpage;

import com.bekrenovr.ecommerce.catalog.item.ItemResponse;
import com.bekrenovr.ecommerce.catalog.util.swagger.ItemResponsePageModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items/landing-page")
@RequiredArgsConstructor
@Tag(name = "LandingPageController")
public class LandingPageController {
    private final LandingPageService landingPageService;

    @Operation(summary = "Get items on landing page")
    @ApiResponse(responseCode = "200", description = "Page with landing page items",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ItemResponsePageModel.class)))
    @GetMapping
    public ResponseEntity<Page<ItemResponse>> getLandingPageItems(
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "${custom.page.default-size}") Integer pageSize
    ){
        return ResponseEntity.ok(landingPageService.getLandingPageItems(pageNumber, pageSize));
    }

    @Operation(summary = "Add item to landing page")
    @ApiResponse(responseCode = "200", description = "Items successfully added")
    @ApiResponse(responseCode = "400", description = "Invalid list of ids supplied (list is empty or all items already added)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public void addLandingPageItems(@RequestParam("ids") List<UUID> itemsIds){
        landingPageService.addLandingPageItems(itemsIds);
    }

    @Operation(summary = "Remove items from landing page")
    @ApiResponse(responseCode = "200", description = "Items successfully removed")
    @DeleteMapping
    public void removeLandingPageItems(@RequestParam("ids") List<UUID> itemsIds){
        landingPageService.removeLandingPageItems(itemsIds);
    }
}
