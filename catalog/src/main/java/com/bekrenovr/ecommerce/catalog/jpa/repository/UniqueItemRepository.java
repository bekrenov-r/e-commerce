package com.bekrenovr.ecommerce.catalog.jpa.repository;

import com.bekrenovr.ecommerce.catalog.model.entity.UniqueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UniqueItemRepository extends JpaRepository<UniqueItem, UUID> {
    UniqueItem findByItemIdAndSize(UUID itemId, String size);
}
