package com.ecommerce.itemsdata.util;

import com.ecommerce.itemsdata.model.Color;
import com.ecommerce.itemsdata.model.ColorEnum;
import com.ecommerce.itemsdata.model.Material;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringParser {

    private static final String PRICE_RANGE_REGEX = "^(0|[1-9]\\d*).(0|[1-9]\\d*)$";
//    private static final String DELIMITER_REGEX = "\\.";

    public static DoubleRange parseDoubleRange(String s){
        if(Pattern.matches(PRICE_RANGE_REGEX, s)){
            Double min = Double.parseDouble(s.substring(0, s.indexOf(".")));
            Double max = Double.parseDouble(s.substring(s.indexOf(".") + 1));
            if(min >= max){
                throw new IllegalArgumentException("Invalid price range: min must be less than max, rejected value: " + s);
            }
            return new DoubleRange(min, max);
        } else {
            throw new IllegalArgumentException("Invalid price range format: " + s);
        }
    }

    public static List<String> parseDelimitedList(String s, String delimiterRegex) {
        List<String> result = Arrays.asList(s.split(delimiterRegex));
        for(String str : result){
            if(str.isBlank() || str.isEmpty())
                throw new IllegalArgumentException("Invalid format of delimited list: \"" + s + "\"");
        }
        return result;
    }

    public static List<ColorEnum> parseColors(String colors) {
        List<String> colorsStr = parseDelimitedList(colors, "\\.");

        List<String> allColorsStringValuesLowerCase =
                Arrays.stream(ColorEnum.values())
                        .map(Enum::name)
                        .map(String::toLowerCase)
                        .toList();

        return colorsStr.stream()
                .map(String::toLowerCase)
                .filter(allColorsStringValuesLowerCase::contains)
                .map(ColorEnum::valueOf)
                .collect(Collectors.toList());
    }

    public static List<Material> parseMaterials(String materials) {
        List<String> materialStr = parseDelimitedList(materials, "\\.");

        List<String> allMaterialsStringValuesLowerCase =
                Arrays.stream(Material.values())
                        .map(Enum::name)
                        .map(String::toLowerCase)
                        .toList();

        return materialStr.stream()
                .map(String::toLowerCase)
                .filter(allMaterialsStringValuesLowerCase::contains)
                .map(Material::valueOf)
                .collect(Collectors.toList());
    }
}
