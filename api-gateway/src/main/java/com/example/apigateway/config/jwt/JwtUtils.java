package com.example.apigateway.config.jwt;

import io.jsonwebtoken.Jwts;

public class JwtUtils {

    public final static String ACCESS_TOKEN_HEADER_NAME = "ACCESS-TOKEN";
    public final static String ROLE = "role";
    public final static String ADMIN = "ADMIN";

    public static String getEmailByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String getRoleByToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(ROLE);
    }
}
