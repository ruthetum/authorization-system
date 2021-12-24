package com.example.userservice.config.jwt;

import com.example.userservice.config.key.JwtKey;
import com.example.userservice.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;

import java.security.Key;
import java.util.Date;

import static com.example.userservice.util.TypeConverter.convertStringToRole;

public class JwtUtils {

    public final static String ACCESS_TOKEN_HEADER_NAME = "ACCESS-TOKEN";
    public final static int ACCESS_TOKEN_EXPIRATION_TIME = 10 * 60 * 1000; // 10분
    public final static String REFRESH_TOKEN_HEADER_NAME = "REFRESH-TOKEN";
    public final static int REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7일
    public final static String ROLE = "role";

    public static Pair<String, String> createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put(ROLE, convertStringToRole(user.getRole()));
        Date now = new Date();
        Pair<String, Key> key = JwtKey.getRandomJwtKey();

        return Pair.of(
                Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                    .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
                    .signWith(key.getSecond())
                    .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                        .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
                        .signWith(key.getSecond())
                        .compact()
        );
    }

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
