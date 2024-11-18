package com.bekrenovr.ecommerce.catalog.item;

import com.bekrenovr.ecommerce.catalog.brand.Brand;
import com.bekrenovr.ecommerce.catalog.category.Category;
import com.bekrenovr.ecommerce.catalog.category.subcategory.Subcategory;
import com.bekrenovr.ecommerce.catalog.item.details.ItemDetails;
import com.bekrenovr.ecommerce.catalog.item.filters.Color;
import com.bekrenovr.ecommerce.catalog.item.filters.Gender;
import com.bekrenovr.ecommerce.catalog.item.filters.Material;
import com.bekrenovr.ecommerce.catalog.item.filters.Season;
import com.bekrenovr.ecommerce.catalog.item.image.ItemImage;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItem;
import com.bekrenovr.ecommerce.common.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, exclude = "itemDetails")
public class Item extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "price_after_discount")
    private Double priceAfterDiscount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id")
    private Subcategory subcategory;

    @OneToMany(mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<ItemImage> images;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @Column(name = "material")
    @Enumerated(EnumType.STRING)
    private Material material;

    @Column(name = "season")
    @Enumerated(EnumType.STRING)
    private Season season;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "item_code")
    private String itemCode;

    @OneToOne(mappedBy = "item",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ItemDetails itemDetails;

    @OneToMany(mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<UniqueItem> uniqueItems;

    public Item(UUID id, String name, String description, Double price, Double discount, Category category, Subcategory subcategory, List<ItemImage> images, Gender gender, String collection, Brand brand, Material material, Season season, Double rating, String itemCode, List<UniqueItem> uniqueItems, ItemDetails itemDetails) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.priceAfterDiscount = calculatePriceAfterDiscount(price, discount);
        this.category = category;
        this.subcategory = subcategory;
        this.images = images;
        this.gender = gender;
        this.brand = brand;
        this.material = material;
        this.season = season;
        this.rating = rating;
        this.itemCode = itemCode;
        this.uniqueItems = uniqueItems;
        this.itemDetails = itemDetails;
    }

    public void setItemDetails(ItemDetails itemDetails) {
        this.itemDetails = itemDetails;
        this.itemDetails.setItem(this);
    }

    public static Double calculatePriceAfterDiscount(Double price, Double discount){
        BigDecimal priceBD = BigDecimal.valueOf(price);
        BigDecimal discountBD = BigDecimal.valueOf(discount);
        BigDecimal priceAfterDiscount =
                priceBD.subtract(priceBD.multiply(discountBD)).setScale(2, RoundingMode.HALF_UP);
        return priceAfterDiscount.doubleValue();
    }

    public void detachImage(ItemImage image) {
        this.images.remove(image);
    }
}
