package de.bwl.bwfla.emucomp.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BWFLAExceptionMapper implements ExceptionMapper<BWFLAException> {

    @Override
    public Response toResponse(BWFLAException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }
}
