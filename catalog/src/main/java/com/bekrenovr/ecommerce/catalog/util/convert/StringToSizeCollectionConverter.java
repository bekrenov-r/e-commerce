package com.bekrenovr.ecommerce.catalog.util.convert;

import com.bekrenovr.ecommerce.catalog.item.size.Size;
import com.bekrenovr.ecommerce.catalog.item.size.SizeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class StringToSizeCollectionConverter implements Converter<String, Collection<Size>> {
    private final SizeFactory sizeFactory;

    @Override
    public Collection<Size> convert(String source) {
        List<String> sizeValues = Arrays.stream(source.split(","))
                .map(String::trim)
                .toList();
        return sizeValues.stream()
                .map(sizeFactory::getSize)
                .toList();
    }
}
