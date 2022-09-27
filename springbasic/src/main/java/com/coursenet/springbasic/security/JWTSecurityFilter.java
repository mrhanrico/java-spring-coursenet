package com.coursenet.springbasic.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTSecurityFilter extends BasicAuthenticationFilter {
	private JWTUtil jwtUtil;
	
	public JWTSecurityFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
	
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}
	
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		// Ambil token
		String token = request.getHeader("Authorization");
		if (token == null || !token.startsWith("Bearer")) {
			log.error("Token is not start with Bearer");
			return null;
		}
		
		DecodedJWT decodedJWT = jwtUtil.decodeJWTToken(token);
		String userName = decodedJWT.getSubject();
		String tokenType = decodedJWT.getClaim("type").asString();
		if (!tokenType.equalsIgnoreCase("ACCESS")) {
			log.error("Token is not access Token");
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(userName, null, new ArrayList<>());
	}

}
