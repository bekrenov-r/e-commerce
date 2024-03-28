package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptions;
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
import java.util.HashSet;

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

    public static Specification<Item> hasSizeIn(Collection<String> sizeValues){
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

    public static Specification<Item> fromFilterOptions(FilterOptions filterOptions){
        Collection<Specification<Item>> specifications = new HashSet<>();
        if(filterOptions.priceRange() != null){
            specifications.add(hasPriceWithinRange(filterOptions.priceRange()));
        }
        if(filterOptions.sizes() != null){
            specifications.add(hasSizeIn(filterOptions.sizes()));
        }
        if(filterOptions.colors() != null){
            specifications.add(hasColorIn(filterOptions.colors()));
        }
        if(filterOptions.brands() != null){
            specifications.add(hasBrandIn(filterOptions.brands()));
        }
        if(filterOptions.materials() != null){
            specifications.add(hasMaterialIn(filterOptions.materials()));
        }
        if(filterOptions.season() != null){
            specifications.add(hasSeason(filterOptions.season()));
        }
        if(filterOptions.rating() != null){
            specifications.add(hasRatingGreaterThan(filterOptions.rating()));
        }
        return Specification.allOf(specifications);
    }
}
