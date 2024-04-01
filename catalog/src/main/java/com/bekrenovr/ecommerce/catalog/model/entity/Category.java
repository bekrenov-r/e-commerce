package com.bekrenovr.ecommerce.catalog.model.entity;

import com.bekrenovr.ecommerce.catalog.model.enums.CategoryEnum;
import com.bekrenovr.ecommerce.common.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "enum_value")
    @Enumerated(EnumType.STRING)
    private CategoryEnum enumValue;

    @Column(name = "img_name")
    private String imageName;

    @OneToMany(mappedBy = "category",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private List<Subcategory> subcategories;
}
