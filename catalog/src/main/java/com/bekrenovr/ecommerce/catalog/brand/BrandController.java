package com.bekrenovr.ecommerce.catalog.brand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@Tag(name = "BrandController")
public class BrandController {
    private final BrandService brandService;

    @Operation(summary = "Get all brands")
    @ApiResponse(
            responseCode = "200",
            description = "List with all brands",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Brand.class))
            )
    )
    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands(){
        return ResponseEntity.ok(brandService.getAllBrands());
    }
}
