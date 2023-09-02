package com.ecommerce.itemsdata.util;

import java.util.Collection;

public class StringUtils {

    public static boolean containsAll(String str, Collection<String> containedCollection){
        return containedCollection.stream().allMatch(str::contains);
    }

}
