package com.ecommerce.itemsdata.repository;

import com.ecommerce.itemsdata.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    List<Subcategory> findAllByCategory_Id(Long categoryId);

}
