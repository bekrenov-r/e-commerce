package com.ecommerce.itemsdata.repository;

import com.ecommerce.itemsdata.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByCategory(Category category);
    List<Item> findAllByGenderAndCategory(Gender gender, Category category);
    List<Item> findAllByGenderAndCategoryAndSubcategory(Gender gender, Category category, Subcategory subcategory);
    List<Item> findAllByAgeGroupAndGenderAndCategory(AgeGroup ageGroup, Gender gender, Category category);
    List<Item> findAllByAgeGroupAndGenderAndCategoryAndSubcategory(
            AgeGroup ageGroup, Gender gender, Category category, Subcategory subcategory
    );

}
