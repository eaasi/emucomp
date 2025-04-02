package de.bwl.bwfla.emucomp.common.exceptions;

import de.bwl.bwfla.emucomp.exceptions.BWFLAException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BWFLAExceptionMapper implements ExceptionMapper<de.bwl.bwfla.emucomp.exceptions.BWFLAException> {

    @Override
    public Response toResponse(BWFLAException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }
}
