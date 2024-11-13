package com.bekrenovr.ecommerce.catalog.item.uniqueitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UniqueItemRepository extends JpaRepository<UniqueItem, UUID> {
    UniqueItem findByItemIdAndSize(UUID itemId, String size);
}
