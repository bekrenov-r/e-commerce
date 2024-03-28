package com.bekrenovr.ecommerce.catalog.util;

import com.bekrenovr.ecommerce.catalog.model.entity.Brand;
import com.bekrenovr.ecommerce.catalog.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StringToBrandCollectionConverter implements Converter<String, Collection<Brand>> {
    private final BrandRepository brandRepository;
    @Override
    public Collection<Brand> convert(String source) {
        Collection<UUID> brandsIds = Arrays.stream(source.split(","))
                .map(UUID::fromString)
                .collect(Collectors.toSet());
        return brandRepository.findAllById(brandsIds);
    }
}
