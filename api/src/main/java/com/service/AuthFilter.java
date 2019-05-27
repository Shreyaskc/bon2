package com.service;

import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/**
 * 
 * @author Shreyas
 *
 */
public class AuthFilter implements ContainerRequestFilter {
    private final static WebApplicationException emptyToken = new WebApplicationException(
	    Response.status(Status.UNAUTHORIZED).entity("Authorization token missing").build());
    private final static WebApplicationException Invalid = new WebApplicationException(
	    Response.status(Status.UNAUTHORIZED).entity("Invalid Token").build());

    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException {
	try {

	    final SecurityContext currentSecurityContext = containerRequest.getSecurityContext();
	    containerRequest.setSecurityContext(new SecurityContext() {

		@Override
		public Principal getUserPrincipal() {
		    return () -> "".toString();
		}

		@Override
		public boolean isUserInRole(String role) {
		    return true;
		}

		@Override
		public boolean isSecure() {
		    return currentSecurityContext.isSecure();
		}

		@Override
		public String getAuthenticationScheme() {
		    return AUTHENTICATION_SCHEME;
		}

	    });

	} catch (Exception e) {
	    LOG.error("Stack Trace : " + ExceptionUtils.getFullStackTrace(e));
	    throw Invalid;
	}
	return containerRequest;

    }

}
