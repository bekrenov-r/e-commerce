package com.ecommerce.salesdataservice;

import com.ecommerce.itemsdata.util.StringParser;
import org.apache.commons.lang.math.IntRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) {

        /*List<String> requestedSizes = new ArrayList<>(Arrays.asList("M", "XL", "S", "XS"));
        List<String> itemSizes = new ArrayList<>(Arrays.asList("M", "XL", "2XL"));
        System.out.println(requestedSizes.retainAll(itemSizes));
        System.out.println(requestedSizes);*/
        List<Boolean> l = Arrays.asList(false, false, false, false);
        boolean bool = l.stream().reduce(true, (a,b) -> a && b);
        System.out.println(bool);

    }

}
