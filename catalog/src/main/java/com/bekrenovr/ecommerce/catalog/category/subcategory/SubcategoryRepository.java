package com.bekrenovr.ecommerce.catalog.category.subcategory;

import com.bekrenovr.ecommerce.catalog.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, UUID> {
    List<Subcategory> findAllByCategory(Category category);
}
