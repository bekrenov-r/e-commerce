package com.bekrenovr.ecommerce.catalog.mapper;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemMappingService;
import com.bekrenovr.ecommerce.catalog.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.util.request.RequestUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
