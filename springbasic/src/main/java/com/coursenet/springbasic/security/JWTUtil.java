package com.coursenet.springbasic.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coursenet.springbasic.constants.TokenConstants;

@Component
public class JWTUtil {
	@Value("${jwt.token.secret}")
	private String secret;

	@Value("${jwt.token.issuer}")
	private String issuer;
	
	@Value("${jwt.token.accessValid}")
	private int accessTokenValid;
	
	@Value("${jwt.token.refreshValid}")
	private int refreshTokenValid;

	public DecodedJWT decodeJWTToken(String token) {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
		return verifier.verify(token.replace("Bearer ", ""));
	}

	public String generatedJWTToken(String userName, String type) {
		//Create JWT Token
		
		int valid = type.equals(TokenConstants.TOKEN_ACCESS) ? accessTokenValid : refreshTokenValid;
		
		LocalDateTime issuedLocalTime = LocalDateTime.now();

		return JWT.create().withIssuer(issuer).withSubject(userName)
				.withIssuedAt(Date.from(issuedLocalTime.atZone(ZoneId.systemDefault()).toInstant()))
				.withExpiresAt(Date.from(issuedLocalTime.plusSeconds(valid).atZone(ZoneId.systemDefault()).toInstant()))
				.withClaim("type", type).sign(Algorithm.HMAC256(secret));
	}

}
