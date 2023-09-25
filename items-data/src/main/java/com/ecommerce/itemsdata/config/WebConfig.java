package com.ecommerce.itemsdata.config;

import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.service.sort.SortOption;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // todo: try ConverterFactory<String, Enum>
    @Override
    public void addFormatters(FormatterRegistry formatterRegistry){
        formatterRegistry.addConverter(String.class, SortOption.class, SortOption::ofString);
        formatterRegistry.addConverter(String.class, AgeGroup.class, AgeGroup::ofString);
        formatterRegistry.addConverter(String.class, ColorEnum.class, ColorEnum::ofString);
        formatterRegistry.addConverter(String.class, Season.class, Season::ofString);
        formatterRegistry.addConverter(String.class, Material.class, Material::ofString);
        formatterRegistry.addConverter(String.class, Gender.class, Gender::ofString);
    }

}
