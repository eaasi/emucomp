package de.bwl.bwfla.emucomp.api;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/health")
public class Health {
    @GET
    public Response health() {
        return Response.status(Response.Status.OK).build();
    }
}
