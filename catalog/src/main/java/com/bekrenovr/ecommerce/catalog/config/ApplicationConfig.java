package com.bekrenovr.ecommerce.catalog.config;

import com.bekrenovr.ecommerce.catalog.item.filters.Color;
import com.bekrenovr.ecommerce.catalog.item.filters.Gender;
import com.bekrenovr.ecommerce.catalog.item.filters.Material;
import com.bekrenovr.ecommerce.catalog.item.filters.Season;
import com.bekrenovr.ecommerce.catalog.item.size.SizeFactory;
import com.bekrenovr.ecommerce.catalog.item.sorting.SortOption;
import com.bekrenovr.ecommerce.catalog.util.convert.StringToSizeCollectionConverter;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
@RequiredArgsConstructor
public class ApplicationConfig implements WebMvcConfigurer {
    @Value("${custom.strategy.shoes-size.max}")
    private int shoesSizeMax;
    @Value("${custom.strategy.shoes-size.min}")
    private int shoesSizeMin;

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry){
        formatterRegistry.addConverter(String.class, SortOption.class, SortOption::ofString);
        formatterRegistry.addConverter(String.class, Color.class, Color::ofString);
        formatterRegistry.addConverter(String.class, Season.class, Season::ofString);
        formatterRegistry.addConverter(String.class, Material.class, Material::ofString);
        formatterRegistry.addConverter(String.class, Gender.class, Gender::ofString);
        formatterRegistry.addConverter(stringToSizeCollectionConverter());
    }

    @Bean
    public SizeFactory sizeFactory() {
        return new SizeFactory(shoesSizeMin, shoesSizeMax);
    }

    @Bean
    public StringToSizeCollectionConverter stringToSizeCollectionConverter() {
        return new StringToSizeCollectionConverter(sizeFactory());
    }

    @Bean
    public Cloudinary cloudinary(CloudinaryProperties properties) {
        Map<String, Object> map = new HashMap<>();
        map.put("cloud_name", properties.getCloudName());
        map.put("api_key", properties.getApiKey());
        map.put("api_secret", properties.getApiSecret());
        map.put("secure", true);
        return new Cloudinary(map);
    }
}
