package com.bekrenovr.ecommerce.catalog.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class ImageUtils {
    public static byte[] imageToByteArray(String imagePath) {
        try {
            Resource image = new ClassPathResource(imagePath);
            return image.getInputStream().readAllBytes();
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

}
