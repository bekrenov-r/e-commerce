package com.ecommerce.itemsdata.repository;

import com.ecommerce.itemsdata.model.Category;
import com.ecommerce.itemsdata.model.Gender;
import com.ecommerce.itemsdata.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByCategory(Category category);

    List<Item> findAllByGenderAndCategory(Gender gender, Category category);

}
