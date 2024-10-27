package com.bekrenovr.ecommerce.catalog.util.sort;

import com.bekrenovr.ecommerce.catalog.model.Size;

import java.util.Comparator;

public class SizeComparator implements Comparator<Size> {
    @Override
    public int compare(Size size1, Size size2) {
        assertCanCompare(size1, size2);
        return Integer.compare(size1.getNumericValue(), size2.getNumericValue());
    }

    private static void assertCanCompare(Size size1, Size size2) {
        if(!size1.getClass().equals(size2.getClass())) {
            throw new IllegalArgumentException("Incompatible size types: cannot compare %s and %s".formatted(
                    size1.getClass().getSimpleName(), size2.getClass().getSimpleName()
            ));
        }
    }
}
