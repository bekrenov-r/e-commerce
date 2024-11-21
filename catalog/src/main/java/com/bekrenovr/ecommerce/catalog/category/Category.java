package com.bekrenovr.ecommerce.catalog.category;

import com.bekrenovr.ecommerce.catalog.category.subcategory.Subcategory;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.SUBCATEGORY_NOT_FOUND;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "enum_value")
    @Enumerated(EnumType.STRING)
    private CategoryEnum enumValue;

    @Column(name = "img_name")
    private String imageName;

    @OneToMany(mappedBy = "category",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Subcategory> subcategories;

    public Subcategory findSubcategory(UUID subcategoryId) {
        return subcategories.stream()
                .filter(s -> s.getId().equals(subcategoryId))
                .findFirst()
                .orElseThrow(() -> new EcommerceApplicationException(SUBCATEGORY_NOT_FOUND, subcategoryId, this.getId()));
    }
}
