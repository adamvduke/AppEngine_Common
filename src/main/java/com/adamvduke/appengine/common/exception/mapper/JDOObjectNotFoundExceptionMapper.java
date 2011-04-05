package com.adamvduke.appengine.common.exception.mapper;

import javax.jdo.JDOObjectNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.adamvduke.jersey.ext.exception.mapper.base.BaseExceptionMapper;

@Provider
public class JDOObjectNotFoundExceptionMapper extends BaseExceptionMapper implements ExceptionMapper <JDOObjectNotFoundException> {

	@Override
	public Response toResponse( JDOObjectNotFoundException exception ) {

		String message = getFormattedException( exception.getMessage() );
		return Response.status( 404 ).entity( message ).type( MediaType.APPLICATION_JSON ).build();
	}
}