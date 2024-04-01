package com.bekrenovr.ecommerce.catalog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
@Getter
public enum CatalogApplicationExceptionReason {
    CATEGORY_NOT_FOUND("No category found for id: %s", NOT_FOUND),
    SUBCATEGORY_NOT_FOUND("No subcategory with id: [%s] found in category with id: [%s]", NOT_FOUND),
    ITEM_NOT_FOUND("Item with id [%s] not found", NOT_FOUND),
    CANNOT_ADD_ITEMS_TO_LANDING_PAGE("Cannot add items to landing page: either all selected items are already placed on landing page or list is empty", BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

}
