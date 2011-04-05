package com.adamvduke.appengine.common.filter;

import java.util.List;

import com.adamvduke.jersey.ext.exception.UnauthorizedRequestException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthService;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/**
 * Implements a ServletFilter to check the request and ensure that it is a properly signed OAuth
 * request.
 * 
 * @author adamd
 */
public class OAuthFilter implements ContainerRequestFilter {

	private OAuthService oauthService;
	private List <String> protectedResourcePrefixes;

	@Override
	public ContainerRequest filter( ContainerRequest request ) {

		// short circuit the filter if there are no protected resources
		String path = request.getPath();
		boolean protectedResource = accessingProtectedResource( path );
		if ( !protectedResource ) {
			return request;
		}

		// try to get the current user from the oauthService
		// if the request isn't signed correctly this will throw an exception
		try {
			oauthService.getCurrentUser();
		}
		catch ( OAuthRequestException e ) {

			// this should only happen in the case where the request hasn't been property signed
			throw new UnauthorizedRequestException( e.getMessage() );
		}

		// if the execution gets here, the request is correctly signed
		return request;
	}

	private boolean accessingProtectedResource( String path ) {

		if ( protectedResourcePrefixes == null ) {
			return false;
		}
		for ( String prefix : protectedResourcePrefixes ) {
			if ( path.startsWith( prefix ) ) {
				return true;
			}
		}
		return false;
	}

	public void setOauthService( OAuthService oauthService ) {

		this.oauthService = oauthService;
	}

	public void setProtectedResourcePrefixes( List <String> protectedResourcePrefixes ) {

		this.protectedResourcePrefixes = protectedResourcePrefixes;
	}
}
