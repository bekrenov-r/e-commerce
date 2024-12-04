package com.bekrenovr.ecommerce.orders.cart;

import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders/cart")
@RequiredArgsConstructor
@Validated
@Tag(name = "CartController")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Get items in cart")
    @ApiResponse(responseCode = "200", description = "List of cart items",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ItemEntryResponse.class))))
    @GetMapping
    public ResponseEntity<List<ItemEntryResponse>> getCartItems(){
        return ResponseEntity.ok(cartService.getCartItems());
    }

    @Operation(summary = "Add item to cart")
    @ApiResponse(responseCode = "201", description = "Item successfully added to cart")
    @ApiResponse(responseCode = "400", description = "Invalid request body supplied / Item with these parameters is unavailable",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Cart entry with these parameters already exists",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<Void> addItemToCart(@RequestBody ItemEntryRequest request){
        cartService.addItemToCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update cart item")
    @ApiResponse(responseCode = "200", description = "Cart item updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid parameter(s) supplied / Item with these parameters is unavailable",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item is not in cart",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping
    public void updateCartItem(
            @RequestParam("entryId") UUID itemEntryId,
            @RequestParam("quantity") @Positive int quantity,
            @RequestParam("size") String size
    ) {
        cartService.updateCartItem(itemEntryId, quantity, size);
    }

    @Operation(summary = "Remove item from cart")
    @ApiResponse(responseCode = "200", description = "Cart item removed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid parameter(s) supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item is not in cart",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping
    public void removeItemFromCart(@RequestParam("entryId") UUID itemEntryId){
        cartService.removeItemFromCart(itemEntryId);
    }

    @Operation(summary = "Clear cart")
    @ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    @DeleteMapping("/clear")
    public void clearCart(){
        cartService.clearCart();
    }
}
