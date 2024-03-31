package com.bekrenovr.ecommerce.catalog.util.convert;

import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.catalog.model.SizeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
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
