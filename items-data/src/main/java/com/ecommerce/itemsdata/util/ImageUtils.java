package com.ecommerce.itemsdata.util;

import com.ecommerce.itemsdata.model.ItemImage;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Component
public class ImageUtils {

    public List<byte[]> convertAllItemImages(List<ItemImage> itemImages) {
        return itemImages.stream()
                .map(this::imageToByteArray)
                .toList();
    }

    public byte[] imageToByteArray(ItemImage itemImage) {
        try {
            File file = Paths.get(itemImage.getPath()).toAbsolutePath().toFile();
            return FileUtils.readFileToByteArray(file);
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public byte[] imagePathToByteArray(String path) {
        try {
            File file = Paths.get(path).toAbsolutePath().toFile();
            return FileUtils.readFileToByteArray(file);
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

}
