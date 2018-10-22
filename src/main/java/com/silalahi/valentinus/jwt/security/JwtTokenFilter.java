package com.silalahi.valentinus.jwt.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.silalahi.valentinus.jwt.exception.CustomException;

public class JwtTokenFilter extends GenericFilterBean {

	private JwtTokenprovider jwtToken;

	public JwtTokenFilter(JwtTokenprovider jwtToken) {
		this.jwtToken = jwtToken;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		String token = jwtToken.resolveToken((HttpServletRequest) request);

		try {
			if (token != null && jwtToken.validateToken(token)) {
				Authentication auth = token != null ? jwtToken.getAuthentication(token) : null;
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (CustomException e) {
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendError(e.getHttpStatus().value(), e.getMessage());
			return;
		}
		filterChain.doFilter(request, response);
	}

}
