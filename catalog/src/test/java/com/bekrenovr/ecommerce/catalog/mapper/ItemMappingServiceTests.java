package com.bekrenovr.ecommerce.catalog.mapper;

import com.bekrenovr.ecommerce.catalog.datasets.Items;
import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemMappingService;
import com.bekrenovr.ecommerce.catalog.model.Item;
import com.bekrenovr.ecommerce.catalog.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.util.request.RequestScope;
import com.bekrenovr.ecommerce.catalog.util.request.RequestUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemMappingServiceTests {

    @Mock
    ItemRepository itemRepository;

    @Mock
    RequestUtils requestUtils;

    @Spy
    @InjectMocks
    ItemMappingService mappingDataProcessor;

    int limit = 10;


    @ParameterizedTest
    @EnumSource(RequestScope.class)
    void testIsItemPopular(RequestScope requestScope){
        // arrange
        when(requestUtils.getRequestScope()).thenReturn(requestScope);
        List<Item> popularItems = this.selectPopularItems(Items.itemsForPopularityTests(100));
        doReturn(popularItems)
                .when(mappingDataProcessor)
                .fetchPopularItemsInScope(eq(requestScope), any(Item.class));

        // act & assert
        popularItems.forEach(
                i -> assertTrue(mappingDataProcessor.isItemPopular(i)) // assert that tested method returns true for each predefined popular items
        );
    }

    private List<Item> selectPopularItems(List<Item> items){
        return items.stream()
                .sorted(Comparator.comparing(
                        i -> i.getItemDetails().getOrdersCountLastMonth(),
                        (a, b) -> Integer.compare(b, a)))
                .limit(limit)
                .toList();
    }
}
