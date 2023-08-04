package com.ecommerce.itemsdata.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "item_color",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id"))
    private List<Color> colors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "item_size",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "size_id"))
    private List<Size> sizes;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age_group")
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(name = "collection")
    private String collection;

    @Column(name = "brand")
    private String brand;

    @Column(name = "material")
    @Enumerated(EnumType.STRING)
    private Material material;

    @Column(name = "season")
    @Enumerated(EnumType.STRING)
    private Season season;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "reviews_count")
    private Integer reviewsCount;

    @Column(name = "item_code")
    private String itemCode;

    /*@OneToOne(mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.REMOVE})
    private ItemDetails itemDetails;*/

    @OneToMany(mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<UniqueItem> uniqueItems;

    public Item(Long id, String name, String description, Double price, Double discount, Category category, Subcategory subcategory, List<ItemImage> images, List<Color> colors, List<Size> sizes, Gender gender, AgeGroup ageGroup, String collection, String brand, Material material, Season season, Double rating, Integer reviewsCount, String itemCode, List<UniqueItem> uniqueItems) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;

        BigDecimal priceBD = BigDecimal.valueOf(price);
        BigDecimal discountBD = BigDecimal.valueOf(discount);
        BigDecimal priceAfterDiscount =
                priceBD.subtract(priceBD.multiply(discountBD)).round(new MathContext(2));
        this.priceAfterDiscount = priceAfterDiscount.doubleValue();
        this.category = category;
        this.subcategory = subcategory;
        this.images = images;
        this.colors = colors;
        this.sizes = sizes;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.collection = collection;
        this.brand = brand;
        this.material = material;
        this.season = season;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.itemCode = itemCode;
        this.uniqueItems = uniqueItems;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", gender='" + gender + '\'' +
                ", ageGroup='" + ageGroup + '\'' +
                ", collection='" + collection + '\'' +
                ", brand='" + brand + '\'' +
                ", material='" + material + '\'' +
                ", season='" + season + '\'' +
                '}';
    }
}
