package com.bekrenovr.ecommerce.catalog.brand;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.BRAND_NOT_FOUND;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    default Brand findByIdOrThrowDefault(UUID id) {
        return findById(id).orElseThrow(() -> new EcommerceApplicationException(BRAND_NOT_FOUND, id));
    }
}
