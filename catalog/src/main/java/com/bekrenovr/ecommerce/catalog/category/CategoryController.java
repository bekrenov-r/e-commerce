package com.bekrenovr.ecommerce.catalog.category;

import com.bekrenovr.ecommerce.catalog.category.subcategory.SubcategoryResponse;
import com.bekrenovr.ecommerce.catalog.item.filters.Gender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "CategoryController")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @ApiResponse(
            responseCode = "200",
            description = "List of all categories",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class))
            )
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Get image for every category")
    @ApiResponse(responseCode = "200", description = "Map with category id as key and Base64-encoded image as value",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "uuid", value = String.class))))
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/images")
    public ResponseEntity<Map<UUID, byte[]>> getAllCategoryImages(@RequestParam Gender gender){
        return ResponseEntity.ok(categoryService.getAllCategoryImages(gender));
    }

    @Operation(summary = "Get all subcategories in category")
    @ApiResponse(responseCode = "200", description = "List of found subcategories (possibly empty)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SubcategoryResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid UUID supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubcategoryResponse>> getAllSubcategoriesInCategory(@PathVariable UUID categoryId){
        return ResponseEntity.ok(categoryService.getAllSubcategoriesInCategory(categoryId));
    }
}
