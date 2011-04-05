package com.adamvduke.appengine.common.filter;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.adamvduke.jersey.ext.exception.UnauthorizedRequestException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthService;
import com.sun.jersey.spi.container.ContainerRequest;

public class OAuthFilterTest {

	@Test
	public void shouldForwardRequestIfNoProtectedResources() throws OAuthRequestException {

		OAuthService oauthService = mock( OAuthService.class );
		ContainerRequest request = mock( ContainerRequest.class );

		// given
		OAuthFilter filter = new OAuthFilter();
		filter.setOauthService( oauthService );

		// when
		ContainerRequest forwardedRequest = filter.filter( request );

		// then
		Assert.assertSame( request, forwardedRequest );
	}

	@Test
	public void shouldForwardRequestIfNotAccessingProtectedResource() throws OAuthRequestException {

		OAuthService oauthService = mock( OAuthService.class );
		ContainerRequest request = mock( ContainerRequest.class );

		// given
		given( request.getPath() ).willReturn( "/somePath/something.html" );
		List <String> protectedResourcePrefixes = new ArrayList <String>();
		protectedResourcePrefixes.add( "/protected" );
		OAuthFilter filter = new OAuthFilter();
		filter.setOauthService( oauthService );
		filter.setProtectedResourcePrefixes( protectedResourcePrefixes );

		// when
		ContainerRequest forwardedRequest = filter.filter( request );

		// then
		Assert.assertSame( request, forwardedRequest );
	}

	@Test( expected = UnauthorizedRequestException.class )
	public void shouldThrowUnauthorizedException() throws OAuthRequestException {

		OAuthService oauthService = mock( OAuthService.class );
		ContainerRequest request = mock( ContainerRequest.class );

		// given
		given( request.getPath() ).willReturn( "/somePath/something.html" );
		given( oauthService.getCurrentUser() ).willThrow( new OAuthRequestException( "" ) );
		List <String> protectedResourcePrefixes = new ArrayList <String>();
		protectedResourcePrefixes.add( "/somePath/" );
		OAuthFilter filter = new OAuthFilter();
		filter.setOauthService( oauthService );
		filter.setProtectedResourcePrefixes( protectedResourcePrefixes );

		// when
		filter.filter( request );

		// then
		// shouldn't get here
		fail();
	}
}
