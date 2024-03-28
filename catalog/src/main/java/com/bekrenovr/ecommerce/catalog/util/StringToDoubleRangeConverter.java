package com.bekrenovr.ecommerce.catalog.util;

import org.apache.commons.lang.math.DoubleRange;
import org.springframework.core.convert.converter.Converter;

public class StringToDoubleRangeConverter implements Converter<String, DoubleRange> {
    @Override
    public DoubleRange convert(String source) {
        String[] values = source.split(",");
        if(values.length != 2)
            throw new IllegalArgumentException();

        return new DoubleRange(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1])
        );
    }
}
