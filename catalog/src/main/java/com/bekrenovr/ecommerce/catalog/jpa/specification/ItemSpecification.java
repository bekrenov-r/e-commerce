package com.bekrenovr.ecommerce.catalog.jpa.specification;

import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.catalog.model.entity.*;
import com.bekrenovr.ecommerce.catalog.model.enums.Color;
import com.bekrenovr.ecommerce.catalog.model.enums.Gender;
import com.bekrenovr.ecommerce.catalog.model.enums.Material;
import com.bekrenovr.ecommerce.catalog.model.enums.Season;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.apache.commons.lang.math.DoubleRange;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class ItemSpecification {
    public static Specification<Item> hasGender(Gender gender){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gender"), gender);
    }

    public static Specification<Item> hasCategory(Category category){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Item> hasCategoryAndSubcategory(Category category, Subcategory subcategory){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("category"), category),
                        criteriaBuilder.equal(root.get("subcategory"), subcategory)
                );
    }

    public static Specification<Item> hasPriceWithinRange(DoubleRange priceRange){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(
                        root.get("priceAfterDiscount"),
                        priceRange.getMinimumDouble(),
                        priceRange.getMaximumDouble()
                );
    }

    public static Specification<Item> hasSizeIn(Collection<Size> sizes){
        Collection<String> sizeValues = sizes.stream()
                .map(Size::getSizeValue)
                .toList();
        return (root, query, criteriaBuilder) -> {
            Subquery<UniqueItem> uniqueItemSubquery = query.subquery(UniqueItem.class);
            Root<UniqueItem> uniqueItemRoot = uniqueItemSubquery.from(UniqueItem.class);
            uniqueItemSubquery.select(uniqueItemRoot);
            uniqueItemSubquery.where(
                    uniqueItemRoot.get("size").in(sizeValues),
                    criteriaBuilder.equal(uniqueItemRoot.get("item"), root)
            );
            return criteriaBuilder.exists(uniqueItemSubquery);
        };
    }

    public static Specification<Item> hasColorIn(Collection<Color> colors){
        return (root, query, criteriaBuilder) -> root.get("color").in(colors);
    }

    public static Specification<Item> hasBrandIn(Collection<Brand> brands){
        return (root, query, criteriaBuilder) -> root.get("brand").in(brands);
    }

    public static Specification<Item> hasMaterialIn(Collection<Material> materials){
        return (root, query, criteriaBuilder) -> root.get("material").in(materials);
    }

    public static Specification<Item> hasSeason(Season season){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("season"), season);
    }

    public static Specification<Item> hasRatingGreaterThan(Short rating){
        return (root, query, criteriaBuilder) -> criteriaBuilder.gt(root.get("rating"), rating);
    }

    public static Specification<Item> matchesSearchPattern(String searchPattern){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        '%' + searchPattern.toLowerCase() + '%'
                );
    }
}
