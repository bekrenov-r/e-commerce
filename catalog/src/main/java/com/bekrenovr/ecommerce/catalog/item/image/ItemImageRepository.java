package com.bekrenovr.ecommerce.catalog.item.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemImageRepository extends JpaRepository<ItemImage, UUID> {
}
