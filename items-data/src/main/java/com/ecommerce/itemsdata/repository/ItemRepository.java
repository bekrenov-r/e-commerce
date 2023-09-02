package com.ecommerce.itemsdata.repository;

import com.ecommerce.itemsdata.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
            from Item i where i.ageGroup = :ageGroup
            and i.gender = :gender and i.category = :category and i.subcategory = :subcategory
            order by i.itemDetails.ordersCountLastMonth desc
            """)
    List<Item> findPopularByAgeGenderCategoryAndSubcategory(@Param("ageGroup") AgeGroup ageGroup,
                                                                 @Param("gender") Gender gender,
                                                                 @Param("category") Category category,
                                                                 @Param("subcategory") Subcategory subcategory,
                                                                 Pageable pageable);

    @Query("""
            from Item i where i.gender = :gender and i.category = :category and i.subcategory = :subcategory
            order by i.itemDetails.ordersCountLastMonth desc
            """)
    List<Item> findPopularByGenderCategoryAndSubcategory(@Param("gender") Gender gender,
                                                              @Param("category") Category category,
                                                              @Param("subcategory") Subcategory subcategory,
                                                              Pageable pageable);

    @Query("""
            from Item i where i.ageGroup = :ageGroup and i.gender = :gender and i.category = :category
            order by i.itemDetails.ordersCountLastMonth desc
            """)
    List<Item> findPopularByAgeGenderAndCategory(@Param("ageGroup") AgeGroup ageGroup,
                                                      @Param("gender") Gender gender,
                                                      @Param("category") Category category,
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
