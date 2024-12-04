package com.bekrenovr.ecommerce.customers.wishlist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers/wishlist")
@RequiredArgsConstructor
@Tag(name = "WishListController")
public class WishListController {
    private final WishListService wishListService;

    @Operation(summary = "Get wish list items for customer")
    @ApiResponse(responseCode = "200", description = "List of items",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @GetMapping
    public ResponseEntity<?> getForCustomer(){
        return wishListService.getForCustomer();
    }

    @Operation(summary = "Add item to wish list")
    @ApiResponse(responseCode = "201", description = "Item added successfully")
    @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Item is already on wish list",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<Void> addItem(@RequestParam(name = "id") UUID itemId) {
        wishListService.addItem(itemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Remove item from wish list")
    @ApiResponse(responseCode = "200", description = "Item removed successfully")
    @ApiResponse(responseCode = "404", description = "Item is not on wish list",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping
    public void removeItem(@RequestParam(name = "id") UUID itemId) {
        wishListService.removeItem(itemId);
    }

    @Operation(summary = "Clear wish list")
    @ApiResponse(responseCode = "200", description = "Wish list cleared successfully")
    @DeleteMapping("/clear")
    public void clear() {
        wishListService.clear();
    }
}
