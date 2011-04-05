package com.adamvduke.appengine.common.filter;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.adamvduke.jersey.ext.exception.RequestEntityTooLargeException;
import com.sun.jersey.spi.container.ContainerRequest;

public class RequestLengthFilterTest {

	@Before
	public void setup() {

		System.setProperty( "appengine.maximum-request-length", "500" );
	}

	@After
	public void tearDown() {

		System.setProperty( "appengine.maximum-request-length", "" );
	}

	@Test
	public void shouldForwardRequestOfLengthShorterThanMax() {

		System.setProperty( "appengine.maximum-request-length", "500" );
		ContainerRequest request = mock( ContainerRequest.class );

		given( request.getHeaderValue( "Content-Length" ) ).willReturn( "499" );
		given( request.getMethod() ).willReturn( "POST" );

		RequestLengthFilter filter = new RequestLengthFilter();
		ContainerRequest forwardedRequest = filter.filter( request );
		Assert.assertSame( request, forwardedRequest );
	}

	@Test
	public void shouldForwardRequestOfLengthEqualToMax() {

		System.setProperty( "appengine.maximum-request-length", "500" );
		ContainerRequest request = mock( ContainerRequest.class );

		given( request.getHeaderValue( "Content-Length" ) ).willReturn( "500" );
		given( request.getMethod() ).willReturn( "POST" );

		RequestLengthFilter filter = new RequestLengthFilter();
		ContainerRequest forwardedRequest = filter.filter( request );
		Assert.assertSame( request, forwardedRequest );
	}

	@SuppressWarnings( "unused" )
	@Test( expected = RequestEntityTooLargeException.class )
	public void shouldNotForwardRequestOfLengthGreaterThanMax() {

		System.setProperty( "appengine.maximum-request-length", "500" );
		ContainerRequest request = mock( ContainerRequest.class );

		given( request.getHeaderValue( "Content-Length" ) ).willReturn( "501" );
		given( request.getMethod() ).willReturn( "POST" );

		RequestLengthFilter filter = new RequestLengthFilter();
		ContainerRequest forwardedRequest = filter.filter( request );
		fail();
	}

	@SuppressWarnings( "unused" )
	@Test( expected = RequestEntityTooLargeException.class )
	public void shouldNotForwardRequestOfLengthGreaterThanDefaultIfMissingProperty() {

		ContainerRequest request = mock( ContainerRequest.class );

		given( request.getHeaderValue( "Content-Length" ) ).willReturn( "501" );
		given( request.getMethod() ).willReturn( "POST" );

		RequestLengthFilter filter = new RequestLengthFilter();
		ContainerRequest forwardedRequest = filter.filter( request );
		fail();
	}

	public void shouldForwardRequestOfLengthEqualToDefaultIfMissingProperty() {

		ContainerRequest request = mock( ContainerRequest.class );

		given( request.getHeaderValue( "Content-Length" ) ).willReturn( "500" );
		given( request.getMethod() ).willReturn( "POST" );

		RequestLengthFilter filter = new RequestLengthFilter();
		ContainerRequest forwardedRequest = filter.filter( request );
		Assert.assertSame( request, forwardedRequest );
	}

	public void shouldForwardRequestOfLengthLessThanDefaultIfMissingProperty() {

		ContainerRequest request = mock( ContainerRequest.class );

		given( request.getHeaderValue( "Content-Length" ) ).willReturn( "499" );
		given( request.getMethod() ).willReturn( "POST" );

		RequestLengthFilter filter = new RequestLengthFilter();
		ContainerRequest forwardedRequest = filter.filter( request );
		Assert.assertSame( request, forwardedRequest );
	}
}
