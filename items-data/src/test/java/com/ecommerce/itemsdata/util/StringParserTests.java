package com.ecommerce.itemsdata.util;

import com.ecommerce.itemsdata.ItemsDataApplication;
import com.ecommerce.itemsdata.model.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.ecommerce.itemsdata.model.Material.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {ItemsDataApplication.class})
public class StringParserTests {

    @Test
    void parseDelimitedList(){
        String s = "asd.fgh";
        List<String> expected = Arrays.asList("asd", "fgh");

        List<String> list = StringParser.parseDelimitedList(s, "\\.");

        assertIterableEquals(expected, list);
    }

    @Test
    void parseDelimitedList_throwWhenInvalidFormat(){
        String s = ".asd.";

        Executable action = () -> StringParser.parseDelimitedList(s, "\\.");

        assertThrows(IllegalArgumentException.class, action);
    }

    @Test
    void parseMaterials_ignoringCase(){
        String materials = "DENIM.Leather.WoOl.Cotton";
        List<Material> expected = Arrays.asList(denim, leather, wool, cotton);

        List<Material> parsedMaterials = StringParser.parseMaterials(materials);

        assertIterableEquals(expected, parsedMaterials);
    }

}
