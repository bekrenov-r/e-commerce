package com.bekrenovr.ecommerce.common.util;


import java.io.IOException;
import java.io.InputStream;

public abstract class TestUtil {
    public static String getResourceAsString(String path) {
        try(InputStream is = TestUtil.class.getResourceAsStream(path)){
            return new String(is.readAllBytes());
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
