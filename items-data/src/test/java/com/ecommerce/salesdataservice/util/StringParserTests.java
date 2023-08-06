package com.ecommerce.salesdataservice.util;

import com.ecommerce.itemsdata.ItemsDataApplication;
import com.ecommerce.itemsdata.model.Material;
import com.ecommerce.itemsdata.service.filter.ItemFilteringProcessor;
import com.ecommerce.itemsdata.util.StringParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.ecommerce.itemsdata.model.Material.*;

@SpringBootTest(classes = {ItemsDataApplication.class})
public class StringParserTests {

    ItemFilteringProcessor itemFilteringProcessor;

    @Autowired
    public StringParserTests(ItemFilteringProcessor itemFilteringProcessor) {
        this.itemFilteringProcessor = itemFilteringProcessor;
    }

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
