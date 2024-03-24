package com.bekrenovr.ecommerce.catalog.util.dev;

import com.bekrenovr.ecommerce.catalog.model.*;
import com.bekrenovr.ecommerce.catalog.repository.BrandRepository;
import com.bekrenovr.ecommerce.catalog.repository.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.repository.SubcategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ItemGenerator {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final BrandRepository brandRepository;

    private List<Category> categories;
    private List<Brand> brands;
    private final List<Size> allSizesClothes = Arrays.asList(
            new Size(UUID.randomUUID(), "XS", SizeType.CLOTHES),
            new Size(UUID.randomUUID(), "S", SizeType.CLOTHES),
            new Size(UUID.randomUUID(), "M", SizeType.CLOTHES),
            new Size(UUID.randomUUID(), "L", SizeType.CLOTHES),
            new Size(UUID.randomUUID(), "XL", SizeType.CLOTHES),
            new Size(UUID.randomUUID(), "2XL", SizeType.CLOTHES),
            new Size(UUID.randomUUID(), "3XL", SizeType.CLOTHES),
            new Size(UUID.randomUUID(), "4XL", SizeType.CLOTHES)

    );
    private final List<Size> allSizesShoes = Arrays.asList(
            new Size(UUID.randomUUID(), "36", SizeType.SHOES),
            new Size(UUID.randomUUID(), "37", SizeType.SHOES),
            new Size(UUID.randomUUID(), "38", SizeType.SHOES),
            new Size(UUID.randomUUID(), "39", SizeType.SHOES),
            new Size(UUID.randomUUID(), "40", SizeType.SHOES),
            new Size(UUID.randomUUID(), "41", SizeType.SHOES),
            new Size(UUID.randomUUID(), "42", SizeType.SHOES),
            new Size(UUID.randomUUID(), "43", SizeType.SHOES),
            new Size(UUID.randomUUID(), "44", SizeType.SHOES),
            new Size(UUID.randomUUID(), "45", SizeType.SHOES)
    );
    private final List<Color> allColors = Arrays.asList(
            new Color(UUID.randomUUID(), ColorEnum.BLACK),
            new Color(UUID.randomUUID(), ColorEnum.WHITE),
            new Color(UUID.randomUUID(), ColorEnum.RED),
            new Color(UUID.randomUUID(), ColorEnum.YELLOW),
            new Color(UUID.randomUUID(), ColorEnum.GREEN),
            new Color(UUID.randomUUID(), ColorEnum.BLUE),
            new Color(UUID.randomUUID(), ColorEnum.VIOLET),
            new Color(UUID.randomUUID(), ColorEnum.GREY),
            new Color(UUID.randomUUID(), ColorEnum.MULTI)
    );
    private final List<Material> materials = Arrays.stream(Material.values()).toList();

    @PostConstruct
    void postConstruct(){
        categories = categoryRepository.findAll();
        brands = brandRepository.findAll();
    }

    public Item generateItem(){
        Random rand = new Random();

        Category category = categories.get(rand.nextInt(categories.size()));
        List<Subcategory> subcategories = category.getSubcategories();
        Subcategory subcategory = !subcategories.isEmpty()
                ? subcategories.get(rand.nextInt(subcategories.size()))
                : null;
        List<ItemImage> images = Arrays.asList(new ItemImage(null, null, "catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg", null));
        List<Size> sizes = randomNumberOfSizes(
                category.getName().equals("SHOES") ? allSizesShoes : allSizesClothes
        );
        List<Color> colors = randomNumberOfColors(allColors);
        String itemName = capitalize(category.getName()) + " " + (rand.nextInt(100) + 1);
        String description = "Description";
        Double price = rand.nextInt(90) + 10 - 0.01;
        Double discount = BigDecimal.valueOf(rand.nextDouble(0.4))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        Double priceAfterDiscount = this.calculatePriceAfterDiscount(price, discount);
        Gender gender = Gender.values()[rand.nextInt(Gender.values().length)];
        String collection = "Collection";
        Brand brand = brands.get(rand.nextInt(brands.size()));
        Material material = materials.get(rand.nextInt(materials.size()));
        Season season = Season.values()[rand.nextInt(Season.values().length)];
        Double rating = BigDecimal.valueOf(rand.nextDouble(3.0) + 2).round(new MathContext(2)).doubleValue();
        String itemCode = generateItemCode();
        Item result = Item.builder()
                .name(itemName)
                .description(description)
                .price(price)
                .discount(discount)
                .priceAfterDiscount(priceAfterDiscount)
                .category(category)
                .subcategory(subcategory)
                .colors(colors)
                .sizes(sizes)
                .gender(gender)
                .collection(collection)
                .brand(brand)
                .material(material)
                .season(season)
                .rating(rating)
                .itemCode(itemCode)
                .build();
        images.forEach(image -> image.setItem(result));
        result.setImages(images);
        return result;
    }

    public Item generateItemWithDetails(){
        Item item = this.generateItem();
        ItemDetails details = this.generateDetails();
        details.setItem(item);
        item.setItemDetails(details);
        return item;
    }

    private ItemDetails generateDetails() {
        Random rand = new Random();
        LocalDate date = LocalDate.ofEpochDay(rand.nextInt(18262, 19357));
        LocalDateTime createdAt = LocalDateTime.of(date, LocalTime.now());
        Integer ordersCountLastMonth = rand.nextInt(30);
        Integer ordersCountTotal = rand.nextInt(100);
        return new ItemDetails(ordersCountTotal, ordersCountLastMonth, createdAt, UUID.randomUUID());
    }

    public List<Item> generateMultiple(int num){
        return Stream
                .generate(this::generateItem)
                .limit(num)
                .collect(Collectors.toList());
    }

    public List<Item> generateMultipleWithDetails(int num){
        return Stream
                .generate(this::generateItemWithDetails)
                .limit(num)
                .collect(Collectors.toList());
    }

    private String capitalize(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private String generateItemCode(){
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 3; i++){
            sb.append((char) rand.nextInt(65, 91));
        }
        for(int i = 0; i < 5; i++){
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    private List<Size> randomNumberOfSizes(List<Size> sizes){
        Random rand = new Random();
        Collections.shuffle(sizes);
        List<Size> res = new ArrayList<>();
        for(int i = 0; i < rand.nextInt(1,sizes.size() + 1); i++){
            res.add(sizes.get(i));
        }
        return res;
    }

    public Double calculatePriceAfterDiscount(Double price, Double discount){
        BigDecimal priceBD = BigDecimal.valueOf(price);
        BigDecimal discountBD = BigDecimal.valueOf(discount);
        BigDecimal priceAfterDiscount =
                priceBD.subtract(priceBD.multiply(discountBD)).setScale(2, RoundingMode.HALF_UP);
        return priceAfterDiscount.doubleValue();
    }

    private List<Color> randomNumberOfColors(List<Color> colors){
        Random rand = new Random();
        Collections.shuffle(colors);
        List<Color> res = new ArrayList<>();
        for(int i = 0; i < rand.nextInt(1,colors.size()); i++){
            res.add(colors.get(i));
        }
        return res;
    }

}
