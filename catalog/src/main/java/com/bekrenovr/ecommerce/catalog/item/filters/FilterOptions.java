package com.bekrenovr.ecommerce.catalog.item.filters;

import com.bekrenovr.ecommerce.catalog.item.size.Size;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterOptions {
        private Gender gender;
        private UUID categoryId;
        private UUID subcategoryId;
        @Schema(example = "50,100")
        private String priceRange;
        @ArraySchema(schema = @Schema(example = "XL", type = "string"))
        private Collection<Size> sizes;
        @ArraySchema(schema = @Schema(example = "BLACK", type = "string", implementation = Color.class))
        private Collection<Color> colors;
        @ArraySchema(schema = @Schema(implementation = UUID.class))
        private Collection<UUID> brandsIds;
        private Collection<Material> materials;
        private Season season;
        private Short rating;
        @jakarta.validation.constraints.Size(min = 3)
        private String searchPattern;
}