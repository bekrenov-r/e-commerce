package com.bekrenovr.ecommerce.catalog.jpa.repository;

import com.bekrenovr.ecommerce.catalog.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
