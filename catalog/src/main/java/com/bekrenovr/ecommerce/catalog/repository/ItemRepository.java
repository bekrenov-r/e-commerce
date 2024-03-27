package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.model.entity.Category;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.model.entity.Subcategory;
import com.bekrenovr.ecommerce.catalog.model.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    List<Item> findAllByGenderAndCategory(Gender gender, Category category);
    List<Item> findAllByGenderAndCategoryAndSubcategory(Gender gender, Category category, Subcategory subcategory);

}
