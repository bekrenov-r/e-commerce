package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.model.ItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDetailsRepository extends JpaRepository<ItemDetails, Long> {

}
