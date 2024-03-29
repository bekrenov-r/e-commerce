package com.bekrenovr.ecommerce.catalog.util.dev;

import com.bekrenovr.ecommerce.catalog.model.ShoesSize;
import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.catalog.model.entity.*;
import com.bekrenovr.ecommerce.catalog.model.enums.*;
import com.bekrenovr.ecommerce.catalog.repository.BrandRepository;
import com.bekrenovr.ecommerce.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bekrenovr.ecommerce.catalog.util.RandomUtils.getRandomElement;
import static com.bekrenovr.ecommerce.catalog.util.RandomUtils.getRandomSeries;

@Component
@RequiredArgsConstructor
public class ItemGenerator {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Value("${custom.strategy.shoes-size.min}")
    private int shoesSizeMin;

    @Value("${custom.strategy.shoes-size.max}")
    private int shoesSizeMax;

    public Item generateItem(){
        Random rand = new Random();

        Category category = getRandomElement(categoryRepository.findAll());
        List<Subcategory> subcategories = category.getSubcategories();
        Subcategory subcategory = !subcategories.isEmpty()
                ? getRandomElement(subcategories)
                : null;
        List<ItemImage> images = Arrays.asList(new ItemImage(null, "catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg", null));
        Color color = getRandomElement(Color.values());
        String itemName = capitalize(category.getName()) + " " + (rand.nextInt(100) + 1);
        String description = "Description";
        Double price = rand.nextInt(90) + 10 - 0.01;
        Double discount = BigDecimal.valueOf(rand.nextDouble(0.4))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        Double priceAfterDiscount = this.calculatePriceAfterDiscount(price, discount);
        Gender gender = getRandomElement(Gender.values());
        String collection = "Collection";
        Brand brand = getRandomElement(brandRepository.findAll());
        Material material = getRandomElement(Material.values());
        Season season = getRandomElement(Season.values());
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
                .color(color)
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
        result.setUniqueItems(generateUniqueItems(result));
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

    private List<UniqueItem> generateUniqueItems(Item item){
        SizeType type = item.getCategory().getEnumValue().equals(CategoryEnum.SHOES) ? SizeType.SHOES : SizeType.CLOTHES;
        List<Size> sizes = type.equals(SizeType.SHOES) ? getRandomShoesSizes() : Arrays.asList(ClothesSize.values());
        return sizes.stream()
                .map(s -> generateUniqueItem(item, s))
                .toList();
    }

    private UniqueItem generateUniqueItem(Item item, Size size){
        Random rand = new Random();
        BigDecimal weightKg = BigDecimal.valueOf(rand.nextDouble());
        return new UniqueItem(
                null,
                size.getSizeValue(),
                weightKg,
                generateBarcode(),
                rand.nextInt(21),
                2, 2,
                item);
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

    private String generateBarcode() {
        final int length = 10;
        Random rand = new Random();
        return Stream
                .generate(
                        () -> String.valueOf(rand.nextInt(10))
                )
                .limit(length)
                .collect(Collectors.joining());
    }

    public Double calculatePriceAfterDiscount(Double price, Double discount){
        BigDecimal priceBD = BigDecimal.valueOf(price);
        BigDecimal discountBD = BigDecimal.valueOf(discount);
        BigDecimal priceAfterDiscount =
                priceBD.subtract(priceBD.multiply(discountBD)).setScale(2, RoundingMode.HALF_UP);
        return priceAfterDiscount.doubleValue();
    }

    private List<Size> getRandomShoesSizes(){
        List<Integer> allShoesSizes = Stream.iterate(
                shoesSizeMin,
                i -> i <= shoesSizeMax,
                i -> i + 1
        ).toList();
        Random random = new Random();
        int numberOfSizes = random.nextInt(allShoesSizes.size()) + 1;
        return getRandomSeries(allShoesSizes, numberOfSizes)
                .stream()
                .map(i -> (Size) new ShoesSize(i))
                .toList();
    }
}
