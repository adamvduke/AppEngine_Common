package com.adamvduke.appengine.common.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

@SuppressWarnings( "serial" )
public class ForwardExcludedPathsFilter extends SpringServlet implements Filter, Servlet, ServletConfig {

	private String excludePatterns;

	@Override
	public void init( FilterConfig filterConfig ) throws ServletException {

		super.init( filterConfig );
		this.excludePatterns = filterConfig.getInitParameter( "excludePatterns" );
	}

	@Override
	public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {

		String url = ( (HttpServletRequest) request ).getRequestURI();
		if ( matchExcludePatterns( url ) ) {
			chain.doFilter( request, response );
			return;
		}
		super.doFilter( request, response, chain );
	}

	private boolean matchExcludePatterns( String url ) {

		Pattern pattern = Pattern.compile( excludePatterns );
		Matcher matcher = pattern.matcher( url );
		boolean matches = matcher.matches();
		return matches;
	}

	@Override
	public void destroy() {

		// TODO Auto-generated method stub

	}
}
