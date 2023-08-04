package com.ecommerce.itemsdata.util.dev;

import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.repository.CategoryRepository;
import com.ecommerce.itemsdata.repository.ColorRepository;
import com.ecommerce.itemsdata.repository.SizeRepository;
import com.ecommerce.itemsdata.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ecommerce.itemsdata.model.SizeType.*;
import static com.ecommerce.itemsdata.model.ColorEnum.*;

@Component
@RequiredArgsConstructor
public class ItemGenerator {

//    private static final List<String> itemNames = Arrays.asList("Shirt", "Pants", "Dress", "Skirt", "Blouse", "Jacket", "Coat", "Sweater", "Shorts", "Jeans", "T-Shirt", "Blazer", "Suit", "Socks", "Shoes", "Boots", "Sandals", "Sneakers");

    private final List<Category> categories = Arrays.asList(
            new Category(1L, "T_SHIRTS", List.of()),
            new Category(2L, "SHIRTS", List.of()),
            new Category(3L, "TROUSERS", Arrays.asList(
                    new Subcategory(1L, "JEANS", null),
                    new Subcategory(2L, "JOGGERS", null),
                    new Subcategory(3L, "SPORT", null))
            ),
            new Category(4L, "SHORTS", List.of()),
            new Category(5L, "HOODIES_AND_SWEATSHIRTS", List.of()),
            new Category(4L, "SWEATERS", List.of()),
            new Category(4L, "COATS", List.of()),
            new Category(4L, "JACKETS", List.of()),
            new Category(4L, "SHOES", Arrays.asList(
                    new Subcategory(4L, "SANDALS", null),
                    new Subcategory(5L, "SNEAKERS", null),
                    new Subcategory(6L, "BOOTS", null))
            ),
            new Category(4L, "UNDERWEAR", List.of()),
            new Category(4L, "SOCKS", List.of())
    );
    private final List<Size> allSizes = Arrays.asList(
            new Size(1L, "XS", clothes),
            new Size(2L, "S", clothes),
            new Size(3L, "M", clothes),
            new Size(4L, "L", clothes),
            new Size(5L, "XL", clothes),
            new Size(6L, "2XL", clothes),
            new Size(7L, "3XL", clothes),
            new Size(8L, "4XL", clothes),
            new Size(9L, "36", shoes),
            new Size(10L, "37", shoes),
            new Size(11L, "38", shoes),
            new Size(12L, "39", shoes),
            new Size(13L, "40", shoes),
            new Size(14L, "41", shoes),
            new Size(15L, "42", shoes),
            new Size(16L, "43", shoes),
            new Size(17L, "44", shoes),
            new Size(18L, "45", shoes)

    );
    private final List<Color> allColors = Arrays.asList(
            new Color(1L, black),
            new Color(2L, white),
            new Color(3L, red),
            new Color(4L, yellow),
            new Color(5L, green),
            new Color(6L, blue),
            new Color(7L, violet),
            new Color(8L, grey),
            new Color(9L, multi)
    );
    private final List<String> brands = Arrays.asList("Louis Vuitton", "Gucci", "Balenciaga", "Dior Homme", "Prada", "Salvatore Ferragamo", "Chanel", "Armani", "Yves Saint Laurent", "Burberry", "Hermès", "Adidas", "Lululemon", "Zara", "UNIQLO", "H&M", "Cartier", "Tiffany & Co.", "Moncler", "Rolex", "Patek Philippe");
    private final List<Material> materials = Arrays.stream(Material.values()).toList();
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;

    public Item generate(){
        Random rand = new Random();
//        List<Category> categories = categoryRepository.findAll();
//        List<Size> allSizes = sizeRepository.findAll();
//        List<Color> allColors = colorRepository.findAll();

        Category category = categories.get(rand.nextInt(categories.size()));
        List<Subcategory> subcategories = category.getSubcategories();
        Subcategory subcategory = null;
        if(!subcategories.isEmpty()){
            subcategory = subcategories.get(rand.nextInt(subcategories.size()));
        }
        List<Size> sizes = randomNumberOfSizes(allSizes);
        List<Color> colors = randomNumberOfColors(allColors);
        String itemName = capitalize(category.getName()) + " " + (rand.nextInt(100) + 1);
        String description = "Description";
        Double price = rand.nextInt(90) + 10 - 0.01;
        Double discount = BigDecimal.valueOf(rand.nextDouble(0.4)).round(new MathContext(2)).doubleValue();
        Gender gender = Gender.values()[rand.nextInt(Gender.values().length)];
        AgeGroup ageGroup = AgeGroup.values()[rand.nextInt(AgeGroup.values().length)];
        String collection = "Collection";
        String brand = brands.get(rand.nextInt(brands.size()));
        Material material = materials.get(rand.nextInt(materials.size()));
        Season season = Season.values()[rand.nextInt(Season.values().length)];
        Double rating = BigDecimal.valueOf(rand.nextDouble(3.0) + 2).round(new MathContext(2)).doubleValue();
        int reviewsCount = rand.nextInt(1000);
        String itemCode = generateItemCode();
        return new Item(
                0L,
                itemName,
                description,
                price,
                discount,
                category,
                subcategory,
                null,
                colors,
                sizes,
                gender,
                ageGroup,
                collection,
                brand,
                material,
                season,
                rating,
                reviewsCount,
                itemCode,
                null
        );
    }

    public List<Item> generateMultiple(int num){
        return Stream
                .generate(this::generate)
                .limit(num)
                .toList();
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
