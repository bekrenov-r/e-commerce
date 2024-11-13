package com.bekrenovr.ecommerce.catalog.integration;

import com.bekrenovr.ecommerce.catalog.item.filters.Color;
import com.bekrenovr.ecommerce.catalog.item.filters.Gender;
import com.bekrenovr.ecommerce.catalog.item.filters.Material;
import com.bekrenovr.ecommerce.catalog.item.filters.Season;
import com.bekrenovr.ecommerce.catalog.item.sorting.SortOption;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemIT {
    @Autowired
    TestRestTemplate restTemplate;

    @Nested
    class GetByCriteria {
        static final String URI_MAPPING = "/items";
        static final String CATEGORY_ID = "a719d81d-e219-4e2e-b2d4-f5432198e783";
        static final String SUBCATEGORY_ID = "27e1d04b-1b23-4b29-aabd-228b98d7ef1f";
        static final String BRAND_ID = "009d93c7-9014-453d-bb4e-fb1b4e51f304";

        @Test
        void shouldReturn200_WhenNoRequestParamsProvided() {
            ResponseEntity<Object> response = restTemplate.getForEntity(URI_MAPPING, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200_WhenAllRequestParamsProvided() {
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("sort", SortOption.BY_PRICE_DESC.getString())
                    .queryParam("colors", Color.BLACK)
                    .queryParam("rating", 4)
                    .queryParam("materials", Material.COTTON)
                    .queryParam("season", Season.MULTISEASON)
                    .queryParam("brandsIds", BRAND_ID)
                    .queryParam("sizes", String.join(",", "L", "XL"))
                    .queryParam("categoryId", CATEGORY_ID)
                    .queryParam("subcategoryId", SUBCATEGORY_ID)
                    .queryParam("gender", Gender.MEN)
                    .queryParam("priceRange", String.join(",", "10", "100"))
                    .queryParam("searchPattern", "shoes")
                    .queryParam("pageSize", 10)
                    .queryParam("pageNumber", 0)
                    .buildAndExpand()
                    .toUri();
            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    class GetById {
        static final String URI_MAPPING = "/items/{id}";
        @Test
        void shouldReturn200_WhenItemExists() {
            String id = "fa5f648e-b2b8-4f8a-a0d7-d65a4547f8d6";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .build(id);

            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenItemDoesntExist() {
            String id = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .build(id);

            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class GetByIds {
        static final String URI_MAPPING = "/items/list";
        @Test
        void shouldReturn200_WhenAllItemsExist() {
            String ids = String.join(",",
                    "fa5f648e-b2b8-4f8a-a0d7-d65a4547f8d6",
                    "b867560e-a92f-44a3-8096-a0f088034460",
                    "af535b75-4050-4db3-bcde-5da06ddb2ae2");
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("ids", ids)
                    .build()
                    .toUri();

            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200_WhenSomeItemsExist() {
            String ids = String.join(",",
                    "fa5f648e-b2b8-4f8a-a0d7-d65a4547f8d6",
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString());
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("ids", ids)
                    .build()
                    .toUri();

            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200_WhenNoItemsExist() {
            String ids = String.join(",",
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString());
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("ids", ids)
                    .build()
                    .toUri();

            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200_WhenIdsEmpty() {
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("ids", "")
                    .build()
                    .toUri();

            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    class GetAllImagesForItem {
        static final String URI_MAPPING = "/items/{itemId}/images";
        @Test
        void shouldReturn200_WhenItemExists() {
            String itemId = "fa5f648e-b2b8-4f8a-a0d7-d65a4547f8d6";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .build(itemId);

            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenItemDoesntExist() {
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .build(itemId);

            ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
