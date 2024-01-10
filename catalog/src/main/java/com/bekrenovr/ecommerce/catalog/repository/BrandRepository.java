package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
