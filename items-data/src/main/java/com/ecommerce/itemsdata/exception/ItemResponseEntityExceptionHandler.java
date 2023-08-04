package com.ecommerce.itemsdata.exception;

import com.ecommerce.itemsdata.util.dev.ErrorDetail;
import com.ecommerce.itemsdata.util.dev.ResponseSource;
import com.ecommerce.itemsdata.util.dev.StandardResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ItemResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {

    public ItemResponseEntityExceptionHandler(ResponseSource responseSource) {
        super(responseSource);
    }

    @Override
    public ResponseEntity<ErrorDetail> handleAllExceptions(Exception ex, WebRequest webRequest){
        ex.printStackTrace();
        return super.handleAllExceptions(ex, webRequest);
    }
}


