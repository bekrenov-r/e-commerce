package com.ecommerce.itemsdata.model;

import com.ecommerce.itemsdata.exception.ItemApplicationException;
import com.ecommerce.itemsdata.exception.ItemApplicationExceptionReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ecommerce.itemsdata.exception.ItemApplicationExceptionReason.AGE_GROUP_NOT_FOUND;

@Getter
@AllArgsConstructor
public enum AgeGroup {

    CHILDREN("children"),
    ADULTS("adults");

    private final String string;

    public static AgeGroup ofString(String s){
        return switch(s){
            case "children" -> CHILDREN;
            case "adults" -> ADULTS;
            default -> throw new ItemApplicationException(AGE_GROUP_NOT_FOUND, s);
        };
    }

}
