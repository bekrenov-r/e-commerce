package com.bekrenovr.ecommerce.catalog.util;

import java.util.*;

public class RandomUtils {
    public static <T> T getRandomElement(Collection<T> collection){
        if(collection == null || collection.isEmpty())
            throw new IllegalArgumentException("Collection cannot be null or empty");
        int randomIndex = new Random().nextInt(collection.size());
        int i = 0;
        for(T element : collection){
            if (i == randomIndex)
                return element;
            i++;
        }
        throw new RuntimeException("Couldn't pick random element from collection");
    }

    public static <T> T getRandomElement(T[] array){
        if(array == null || array.length == 0)
            throw new IllegalArgumentException("Array cannot be null or empty");

        return array[new Random().nextInt(array.length)];
    }

    public static <T> List<T> getRandomSeries(List<T> list, int seriesLength){
        if(list == null || list.isEmpty())
            throw new IllegalArgumentException("List cannot be null or empty");
        if(seriesLength < 0)
            throw new IllegalArgumentException("Series length cannot be negative number");
        if(seriesLength > list.size())
            throw new IllegalArgumentException("Series length cannot be greater than list size");

        Collections.shuffle(list);
        return list.subList(0, seriesLength);
    }

    public static <T> List<T> getRandomSeries(T[] array, int seriesLength){
        if(array == null)
            throw new IllegalArgumentException("Array cannot be null");
        List<T> list = Arrays.asList(array);
        return getRandomSeries(list, seriesLength);
    }
}
