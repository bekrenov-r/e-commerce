package com.bekrenovr.ecommerce.keycloakserver.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.exception.ErrorDetail;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class EcommerceApplicationExceptionMapper implements ExceptionMapper<EcommerceApplicationException> {
    @Override
    public Response toResponse(EcommerceApplicationException ex) {
        log.error(ex.getClass().getSimpleName() + ": ", ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                ex.getReason().getStatus(),
                ex.getMessage()
        );
        return Response.status(ex.getReason().getStatus().value())
                .entity(errorDetail).build();
    }
}
