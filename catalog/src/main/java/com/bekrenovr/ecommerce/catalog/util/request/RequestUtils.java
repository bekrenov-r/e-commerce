package com.bekrenovr.ecommerce.catalog.util.request;

import com.bekrenovr.ecommerce.catalog.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static com.bekrenovr.ecommerce.catalog.util.request.RequestScope.*;

@Component
public class RequestUtils {

    public RequestScope getRequestScope(){
        String requestURI = this.getRequestURI();
        if(StringUtils.containsAll(requestURI, Arrays.asList("gender", "category", "subcategory"))){
            return GENDER_CATEGORY_AND_SUBCATEGORY;
        } else if(StringUtils.containsAll(requestURI, Arrays.asList("gender", "category"))){
            return GENDER_AND_CATEGORY;
        } else if(requestURI.contains("by-season")){
            return SEASON;
        } else {
            return COLLECTION;
        }
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public String getRequestURI() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getRequestURI();
    }

}
