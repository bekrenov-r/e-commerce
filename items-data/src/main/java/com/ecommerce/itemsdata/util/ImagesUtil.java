package com.ecommerce.itemsdata.util;

import com.ecommerce.itemsdata.model.ItemImage;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class ImagesUtil {

    public List<String> encodeAllItemImages(List<ItemImage> itemImages) {
        return itemImages.stream()
                .map(this::encodeItemImage)
                .toList();
    }

    public String encodeItemImage(ItemImage itemImage) {
        try {
            File file = Paths.get(itemImage.getPath()).toAbsolutePath().toFile();
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            return Base64.getEncoder().encodeToString(fileContent);
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

}
