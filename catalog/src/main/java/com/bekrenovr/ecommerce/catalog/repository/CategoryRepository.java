package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
