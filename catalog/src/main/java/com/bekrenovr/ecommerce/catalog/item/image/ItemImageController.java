package com.bekrenovr.ecommerce.catalog.item.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items/{id}/images")
@RequiredArgsConstructor
@Tag(name = "ItemImageController")
public class ItemImageController {
    private final ItemImageService itemImageService;

    @Operation(summary = "Get images by item id")
    @ApiResponse(responseCode = "200", description = "List of images",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ItemImageResponse.class))))
    @ApiResponse(responseCode = "400", description = "Invalid UUID supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping
    public ResponseEntity<List<ItemImageResponse>> getAll(@PathVariable("id") UUID itemId){
        return ResponseEntity.ok(itemImageService.getAll(itemId));
    }

    @Operation(summary = "Upload image for item")
    @ApiResponse(responseCode = "201", description = "Image uploaded successfully")
    @ApiResponse(responseCode = "400", description = "Invalid UUID supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<Void> upload(
            @PathVariable("id")
            UUID itemId,
            @Parameter(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            array = @ArraySchema(schema = @Schema(type = "string", format = "binary")))
            )
            @RequestParam("images")
            List<MultipartFile> images
    ) {
        itemImageService.upload(itemId, images);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Remove image for item")
    @ApiResponse(responseCode = "200", description = "Image removed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid UUID supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping
    public void delete(
            @PathVariable("id")
            UUID itemId,
            @Parameter(array = @ArraySchema(schema = @Schema(type = "string", format = "uuid")))
            @RequestParam
            List<UUID> ids
    ) {
        itemImageService.delete(itemId, ids);
    }
}
