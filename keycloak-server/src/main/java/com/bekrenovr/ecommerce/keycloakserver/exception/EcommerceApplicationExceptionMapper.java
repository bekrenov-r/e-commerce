package com.bekrenovr.ecommerce.keycloakserver.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.exception.ErrorDetail;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class EcommerceApplicationExceptionMapper implements ExceptionMapper<EcommerceApplicationException> {
    @Override
    public Response toResponse(EcommerceApplicationException ex) {
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return Response.status(Response.Status.BAD_REQUEST).entity(errorDetail).build();
    }
}
