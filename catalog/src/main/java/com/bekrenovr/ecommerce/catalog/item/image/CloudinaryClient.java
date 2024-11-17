package com.bekrenovr.ecommerce.catalog.item.image;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class CloudinaryClient {
    private final Cloudinary cloudinary;

    public List<String> uploadImages(List<MultipartFile> images) {
        return images.stream()
                .map(image -> {
                    try {
                        Map<?, ?> response = cloudinary.uploader().upload(image.getBytes(), Collections.EMPTY_MAP);
                        String url = (String)response.get("url");
                        log.info("Successfully uploaded image to Cloudinary. URL: {}", url);
                        return url;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    public void deleteImages(List<String> urls) {
        Collection<String> publicIds = urls.stream()
                .map(this::getPublicId)
                .collect(Collectors.toList());
        try {
            cloudinary.api().deleteResources(publicIds, Collections.EMPTY_MAP);
            log.info("Successfully removed {} image(s) from Cloudinary.", publicIds.size());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getPublicId(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        int extensionIndex = url.lastIndexOf('.');
        return url.substring(lastSlashIndex + 1, extensionIndex);
    }
}
