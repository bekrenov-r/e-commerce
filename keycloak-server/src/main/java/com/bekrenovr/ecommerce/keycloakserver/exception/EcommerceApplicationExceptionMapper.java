package com.bekrenovr.ecommerce.keycloakserver.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;

@Slf4j
public class EcommerceApplicationExceptionMapper implements ExceptionMapper<EcommerceApplicationException> {
    @Override
    public Response toResponse(EcommerceApplicationException ex) {
        log.error(ex.getClass().getSimpleName() + ": ", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getReason().getStatus(), ex.getMessage());
        return Response.status(problemDetail.getStatus())
                .entity(problemDetail).build();
    }
}
