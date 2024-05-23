package com.bekrenovr.ecommerce.catalog.jpa.repository;

import com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationException;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.ITEM_NOT_FOUND;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID>, JpaSpecificationExecutor<Item> {
    default Item findByIdOrThrowDefault(UUID id) {
        return findById(id)
                .orElseThrow(() -> new CatalogApplicationException(ITEM_NOT_FOUND, id));
    }
}
