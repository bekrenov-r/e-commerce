package com.bekrenovr.ecommerce.catalog.config;

import com.bekrenovr.ecommerce.catalog.model.enums.Color;
import com.bekrenovr.ecommerce.catalog.model.enums.Gender;
import com.bekrenovr.ecommerce.catalog.model.enums.Material;
import com.bekrenovr.ecommerce.catalog.model.enums.Season;
import com.bekrenovr.ecommerce.catalog.util.convert.StringToDoubleRangeConverter;
import com.bekrenovr.ecommerce.catalog.util.convert.StringToSizeCollectionConverter;
import com.bekrenovr.ecommerce.catalog.util.sort.SortOption;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final StringToSizeCollectionConverter stringToSizeCollectionConverter;

    // todo: try ConverterFactory<String, Enum>
    @Override
    public void addFormatters(FormatterRegistry formatterRegistry){
        formatterRegistry.addConverter(String.class, SortOption.class, SortOption::ofString);
        formatterRegistry.addConverter(String.class, Color.class, Color::ofString);
        formatterRegistry.addConverter(String.class, Season.class, Season::ofString);
        formatterRegistry.addConverter(String.class, Material.class, Material::ofString);
        formatterRegistry.addConverter(String.class, Gender.class, Gender::ofString);
        formatterRegistry.addConverter(stringToSizeCollectionConverter);
        formatterRegistry.addConverter(new StringToDoubleRangeConverter());
    }

}
