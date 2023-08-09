package com.ecommerce.itemsdata.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ItemApplicationExceptionReason {

    CATEGORY_NOT_FOUND("No category found for id: %s", NOT_FOUND),
    SUBCATEGORY_NOT_FOUND("No subcategory with id: [%s] found in category with id: [%s]", NOT_FOUND),
    AGE_GROUP_NOT_FOUND("No age group found for value: [%s]", NOT_FOUND);


    private final String message;
    private final HttpStatus status;

}
