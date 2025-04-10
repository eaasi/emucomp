package de.bwl.bwfla.emucomp.api.security;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthRequestFilter implements ClientRequestFilter {

    private final String authenticationToken;

    public AuthRequestFilter() {
        this.authenticationToken = MachineTokenProvider.getApiKey();
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        requestContext.getHeaders().add("Authorization", "Bearer " + authenticationToken);
    }
}