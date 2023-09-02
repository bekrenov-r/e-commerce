package com.ecommerce.itemsdata.util.request;

import com.ecommerce.itemsdata.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static com.ecommerce.itemsdata.util.request.RequestScope.*;

@Component
public class RequestUtils {

    public RequestScope getRequestScope(){
        String requestURI = this.getRequestURI();
        if(StringUtils.containsAll(requestURI, Arrays.asList("gender", "age-group", "category", "subcategory"))){
            return AGE_GENDER_CATEGORY_AND_SUBCATEGORY;
        } else if(StringUtils.containsAll(requestURI, Arrays.asList("gender", "category", "subcategory"))){
            return GENDER_CATEGORY_AND_SUBCATEGORY;
        } else if(StringUtils.containsAll(requestURI, Arrays.asList("age-group", "gender", "category"))){
            return AGE_GENDER_AND_CATEGORY;
        } else if(StringUtils.containsAll(requestURI, Arrays.asList("gender", "category"))){
            return GENDER_AND_CATEGORY;
        } else if(requestURI.contains("by-season")){
            return SEASON;
        } else {
            return COLLECTION;
        }
    }

    public String getRequestURI() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getRequestURI();
    }

}
