package com.bekrenovr.ecommerce.catalog.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ImageUtils {
    public static byte[] imageToByteArray(String imagePath) {
        try {
            File file = Paths.get(imagePath).toAbsolutePath().toFile();
            return FileUtils.readFileToByteArray(file);
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

}
