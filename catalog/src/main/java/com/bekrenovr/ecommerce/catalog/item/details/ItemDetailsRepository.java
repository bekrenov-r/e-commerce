package com.bekrenovr.ecommerce.catalog.item.details;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemDetailsRepository extends JpaRepository<ItemDetails, UUID> {

}
