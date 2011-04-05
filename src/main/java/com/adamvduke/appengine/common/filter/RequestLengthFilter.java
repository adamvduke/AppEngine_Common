package com.adamvduke.appengine.common.filter;

import com.adamvduke.jersey.ext.exception.RequestEntityTooLargeException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class RequestLengthFilter implements ContainerRequestFilter {

	public static final String DEFAULT_MAX_REQUEST_LENGTH = "500";

	private static final Integer defaultMaximumRequestLength = Integer.valueOf( DEFAULT_MAX_REQUEST_LENGTH );

	@Override
	public ContainerRequest filter( ContainerRequest request ) {

		int maxRequestLength;

		try {
			String maxRequestLengthString = System.getProperty( "appengine.maximum-request-length" );
			maxRequestLength = Integer.parseInt( maxRequestLengthString );
		}
		catch ( NumberFormatException e ) {
			maxRequestLength = defaultMaximumRequestLength;
		}
		if ( request.getMethod().equals( "POST" ) ) {

			String contentLengthHeader = request.getHeaderValue( "Content-Length" );
			int contentLength = Integer.parseInt( contentLengthHeader );
			if ( contentLength > maxRequestLength ) {

				throw new RequestEntityTooLargeException( maxRequestLength );
			}
		}
		return request;
	}
}
