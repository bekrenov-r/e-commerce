package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    List<Item> findAllByCategory(Category category);
    List<Item> findAllByGenderAndCategory(Gender gender, Category category);
    List<Item> findAllByGenderAndCategoryAndSubcategory(Gender gender, Category category, Subcategory subcategory);

    @Query("""
            from Item i where i.gender = :gender and i.category = :category and i.subcategory = :subcategory
            order by i.itemDetails.ordersCountLastMonth desc
            """)
    List<Item> findPopularByGenderCategoryAndSubcategory(@Param("gender") Gender gender,
                                                              @Param("category") Category category,
                                                              @Param("subcategory") Subcategory subcategory,
                                                              Pageable pageable);

    @Query("""
            from Item i where i.gender = :gender and i.category = :category
            order by i.itemDetails.ordersCountLastMonth desc
            """)
    List<Item> findPopularByGenderAndCategory(@Param("gender") Gender gender,
                                                   @Param("category") Category category,
                                                   Pageable pageable);

    @Query("from Item i where i.season = :season order by i.itemDetails.ordersCountLastMonth desc")
    List<Item> findPopularBySeason(@Param("season") Season season, Pageable pageable);

    @Query("from Item i where i.collection = :collection order by i.itemDetails.ordersCountLastMonth desc")
    List<Item> findPopularByCollection(@Param("collection") String collection, Pageable pageable);

}
