package com.bekrenovr.ecommerce.catalog.integration;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemImageIT {
    static final String URI_MAPPING = "/items/{itemId}/images";
    TestRestTemplate restTemplate;

    @MockBean
    Cloudinary cloudinary;

    @Autowired
    ItemImageIT(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Nested
    class GetAll {
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

    @Nested
    class Upload {
        @Test
        void shouldReturn404_WhenItemDoesntExist() throws IOException {
            mockCloudinary();
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING).build(itemId);
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            Resource image = new ClassPathResource("test-image.png");
            bodyBuilder.part("images", image, MediaType.IMAGE_PNG);
            RequestEntity<MultiValueMap<String, HttpEntity<?>>> request = RequestEntity.post(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                    .body(bodyBuilder.build());

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }


        @Test
        void shouldReturn201_WhenUploadingSingleImage() throws IOException {
            mockCloudinary();
            String itemId = "fa5f648e-b2b8-4f8a-a0d7-d65a4547f8d6";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING).build(itemId);
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("images", new ClassPathResource("test-image.png"), MediaType.IMAGE_PNG);
            RequestEntity<MultiValueMap<String, HttpEntity<?>>> request = RequestEntity.post(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                    .body(bodyBuilder.build());

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void shouldReturn201_WhenUploadingSeveralImages() throws IOException {
            mockCloudinary();
            String itemId = "fa5f648e-b2b8-4f8a-a0d7-d65a4547f8d6";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING).build(itemId);
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            Stream.generate(() -> new ClassPathResource("test-image.png"))
                    .limit(3)
                    .forEach(image -> bodyBuilder.part("images", image, MediaType.IMAGE_PNG));
            RequestEntity<MultiValueMap<String, HttpEntity<?>>> request = RequestEntity.post(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                    .body(bodyBuilder.build());

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        private void mockCloudinary() throws IOException {
            Uploader mockUploader = Mockito.mock(Uploader.class);
            when(mockUploader.upload(any(), anyMap()))
                    .thenReturn(Map.of("url", "http://test.resource.com/resource"));
            when(cloudinary.uploader()).thenReturn(mockUploader);
        }
    }
}
