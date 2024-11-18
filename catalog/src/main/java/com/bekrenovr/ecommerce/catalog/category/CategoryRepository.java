package com.bekrenovr.ecommerce.catalog.category;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.CATEGORY_NOT_FOUND;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    default Category findByIdOrThrowDefault(UUID id) {
        return findById(id).orElseThrow(() -> new EcommerceApplicationException(CATEGORY_NOT_FOUND, id));
    };
}
