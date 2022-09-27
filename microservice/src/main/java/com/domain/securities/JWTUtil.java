package com.domain.securities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {
  @Value("${jwt.token.secret}")
  private String secret;

  public DecodedJWT decodeJWTToken(String token) {
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
    return verifier.verify(token.replace("Bearer ", ""));
  }
}
