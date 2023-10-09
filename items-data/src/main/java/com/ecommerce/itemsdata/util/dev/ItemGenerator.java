package com.ecommerce.itemsdata.util.dev;

import com.ecommerce.itemsdata.model.*;
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

import static com.ecommerce.itemsdata.model.ColorEnum.*;
import static com.ecommerce.itemsdata.model.SizeType.CLOTHES;
import static com.ecommerce.itemsdata.model.SizeType.SHOES;

@Component
@RequiredArgsConstructor
public class ItemGenerator {

    private final List<Category> categories = Arrays.asList(
            new Category("t-shirts", "T-Shirts", "t_shirts.png", List.of()),
            new Category("shirts", "Shirts", "shirts.png", List.of()),
            new Category("trousers", "Trousers", "trousers.png", Arrays.asList(
                    new Subcategory(1L, "JEANS", null),
                    new Subcategory(2L, "JOGGERS", null),
                    new Subcategory(3L, "SPORT", null))
            ),
            new Category("shorts", "Shorts", "shorts.png", List.of()),
            new Category("hoodies-and-sweatshirts", "Hoodies & Sweatshirts", "hoodies_and_sweatshirts.png", List.of()),
            new Category("sweaters", "Sweaters", "sweaters.png", List.of()),
            new Category("coats", "Coats", "coats.png", List.of()),
            new Category("jackets", "Jackets", "jackets.png", List.of()),
            new Category("shoes", "Shoes", "shoes.png", Arrays.asList(
                    new Subcategory(4L, "SANDALS",  null),
                    new Subcategory(5L, "SNEAKERS", null),
                    new Subcategory(6L, "BOOTS", null))
            ),
            new Category("underwear", "Underwear", "underwear.png", List.of()),
            new Category("socks", "Socks", "socks.png", List.of()),
            new Category("accessories", "Accessories", "accessories.png", List.of())
    );
    private final List<Size> allSizesClothes = Arrays.asList(
            new Size(1L, "XS", CLOTHES),
            new Size(2L, "S", CLOTHES),
            new Size(3L, "M", CLOTHES),
            new Size(4L, "L", CLOTHES),
            new Size(5L, "XL", CLOTHES),
            new Size(6L, "2XL", CLOTHES),
            new Size(7L, "3XL", CLOTHES),
            new Size(8L, "4XL", CLOTHES)

    );
    private final List<Size> allSizesShoes = Arrays.asList(
            new Size(9L, "36", SHOES),
            new Size(10L, "37", SHOES),
            new Size(11L, "38", SHOES),
            new Size(12L, "39", SHOES),
            new Size(13L, "40", SHOES),
            new Size(14L, "41", SHOES),
            new Size(15L, "42", SHOES),
            new Size(16L, "43", SHOES),
            new Size(17L, "44", SHOES),
            new Size(18L, "45", SHOES)
    );
    private final List<Color> allColors = Arrays.asList(
            new Color(1L, BLACK),
            new Color(2L, WHITE),
            new Color(3L, RED),
            new Color(4L, YELLOW),
            new Color(5L, GREEN),
            new Color(6L, BLUE),
            new Color(7L, VIOLET),
            new Color(8L, GREY),
            new Color(9L, MULTI)
    );
    private final List<Brand> brands = Arrays.asList(
            new Brand(1L, "Louis Vuitton"),
            new Brand(2L, "Gucci"),
            new Brand(3L, "Balenciaga"),
            new Brand(4L, "Dior Homme"),
            new Brand(5L, "Prada"),
            new Brand(6L, "Salvatore Ferragamo"),
            new Brand(7L, "Chanel"),
            new Brand(8L, "Armani"),
            new Brand(9L, "Yves Saint Laurent"),
            new Brand(10L, "Burberry"),
            new Brand(11L, "Hermès"),
            new Brand(12L, "Lululemon"),
            new Brand(13L, "Zara"),
            new Brand(14L, "UNIQLO"),
            new Brand(15L, "H&M"),
            new Brand(16L, "Cartier"),
            new Brand(17L, "Tiffany & Co."),
            new Brand(18L, "Moncler"),
            new Brand(19L, "Rolex"),
            new Brand(20L, "Patek Philippe")
    );
    private final List<Material> materials = Arrays.stream(Material.values()).toList();

    public Item generateItem(){
        Random rand = new Random();

        Category category = categories.get(rand.nextInt(categories.size()));
        List<Subcategory> subcategories = category.getSubcategories();
        Subcategory subcategory = !subcategories.isEmpty()
                ? subcategories.get(rand.nextInt(subcategories.size()))
                : null;
        List<ItemImage> images = Arrays.asList(new ItemImage(null, null, "items-data/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg", null));
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
        AgeGroup ageGroup = AgeGroup.values()[rand.nextInt(AgeGroup.values().length)];
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
                .ageGroup(ageGroup)
                .collection(collection)
                .brand(brand)
                .material(material)
                .season(season)
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
        return new ItemDetails(ordersCountTotal, ordersCountLastMonth, createdAt, 0L);
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
