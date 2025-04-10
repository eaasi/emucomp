package de.bwl.bwfla.emucomp.common.exceptions;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BWFLAExceptionMapper implements ExceptionMapper<BWFLAException> {

    @Override
    public Response toResponse(BWFLAException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }
}
