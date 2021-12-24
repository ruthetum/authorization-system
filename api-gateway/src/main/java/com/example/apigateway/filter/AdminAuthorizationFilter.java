package com.example.apigateway.filter;

import com.example.apigateway.config.jwt.JwtUtils;
import com.example.apigateway.exception.ExpiredTokenException;
import com.example.apigateway.exception.NotAuthorizationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.example.apigateway.config.jwt.JwtUtils.ACCESS_TOKEN_HEADER_NAME;
import static com.example.apigateway.config.jwt.JwtUtils.getRoleByToken;

@Component
@Slf4j
public class AdminAuthorizationFilter  extends AbstractGatewayFilterFactory<AdminAuthorizationFilter.Config> {

    public AdminAuthorizationFilter() {
        super(AdminAuthorizationFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(AdminAuthorizationFilter.Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            List<String> headers = request.getHeaders().get(ACCESS_TOKEN_HEADER_NAME);
            if (Objects.isNull(headers))
                throw new NotAuthorizationException();

            String token = headers.get(0);

            if (!getRoleByToken(token).equals(JwtUtils.ADMIN))
                throw new NotAuthorizationException();

            try {
                String email = JwtUtils.getEmailByToken(token);
                log.info("Admin Authorization : {}", email);
            } catch (ExpiredJwtException e) {
                throw new ExpiredTokenException();
                // setErrorResponse(exchange, HttpStatus.UNAUTHORIZED);
            } catch (IllegalArgumentException | MalformedJwtException e) {
                throw new NotAuthorizationException();
                // setErrorResponse(exchange, HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //
            }));
        });
    }

    public static class Config {

    }

    private Mono<Void> setErrorResponse(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
