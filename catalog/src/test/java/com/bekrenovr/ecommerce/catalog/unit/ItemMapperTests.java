package com.bekrenovr.ecommerce.catalog.unit;

import com.bekrenovr.ecommerce.catalog.brand.Brand;
import com.bekrenovr.ecommerce.catalog.brand.BrandRepository;
import com.bekrenovr.ecommerce.catalog.category.Category;
import com.bekrenovr.ecommerce.catalog.category.CategoryEnum;
import com.bekrenovr.ecommerce.catalog.category.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.category.subcategory.Subcategory;
import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.ItemMapper;
import com.bekrenovr.ecommerce.catalog.item.ItemRequest;
import com.bekrenovr.ecommerce.catalog.item.filters.Color;
import com.bekrenovr.ecommerce.catalog.item.filters.Gender;
import com.bekrenovr.ecommerce.catalog.item.filters.Material;
import com.bekrenovr.ecommerce.catalog.item.filters.Season;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ItemMapperTests {
    private ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Mock
    BrandRepository brandRepository;
    @Mock
    CategoryRepository categoryRepository;

    @BeforeEach
    void mockDependencies() {
        ReflectionTestUtils.setField(itemMapper, "brandRepository", brandRepository);
        ReflectionTestUtils.setField(itemMapper, "categoryRepository", categoryRepository);
    }

    @Test
    void requestToItem_Basic() {
        Brand testBrand = new Brand("test");
        Category testCategory = new Category("test", CategoryEnum.COATS, "test", null);
        List<Subcategory> testSubcategories = Arrays.asList(
                new Subcategory(UUID.randomUUID(), "testSubcategory1", testCategory)
        );
        testCategory.setSubcategories(testSubcategories);
        when(brandRepository.findByIdOrThrowDefault(any(UUID.class))).thenReturn(testBrand);
        when(categoryRepository.findByIdOrThrowDefault(any(UUID.class))).thenReturn(testCategory);
        ItemRequest request = testItemRequest(testSubcategories.get(0).getId());
        Item expected = testItem(testCategory, testCategory.getSubcategories().get(0), testBrand);
        Item actual = itemMapper.requestToItem(request);

        assertEquals(expected, actual);
    }

    @Test
    void requestToItem_ShouldIgnoreSubcategory_WhenSubcategoryIdNull() {
        Brand testBrand = new Brand("test");
        Category testCategory = new Category("test", CategoryEnum.COATS, "test", null);
        List<Subcategory> testSubcategories = Arrays.asList(
                new Subcategory(UUID.randomUUID(), "testSubcategory1", testCategory)
        );
        testCategory.setSubcategories(testSubcategories);
        when(brandRepository.findByIdOrThrowDefault(any(UUID.class))).thenReturn(testBrand);
        when(categoryRepository.findByIdOrThrowDefault(any(UUID.class))).thenReturn(testCategory);
        ItemRequest request = testItemRequest(null);
        Item expected = testItem(testCategory, null, testBrand);

        Item actual = itemMapper.requestToItem(request);

        assertNull(actual.getSubcategory());
        assertEquals(expected, actual);
    }

    static Item testItem(Category category, Subcategory subcategory, Brand brand) {
        return new Item("Name", "Description", 100.0, 0.2, 80.0,
                category, subcategory, null, Color.BLACK, Gender.WOMEN,
                brand, Material.COTTON, Season.SPRING, null, "item code", null, null);
    }

    static ItemRequest testItemRequest(UUID subcategoryId) {
        return new ItemRequest("Name", "Description", 100, 0.2,
                UUID.randomUUID(), subcategoryId, Color.BLACK, Gender.WOMEN,
                UUID.randomUUID(), "item code", Material.COTTON, Season.SPRING, List.of()
        );
    }
}
