package com.example.apigateway.filter;

import com.example.apigateway.util.LogUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalLoggingFilter extends AbstractGatewayFilterFactory<GlobalLoggingFilter.Config> {

    private LogUtil logUtil;

    public GlobalLoggingFilter(LogUtil logUtil) {
        super(Config.class);
        this.logUtil = logUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (config.isPreLogger()) {
                log.info("Global Logging Filter start: request id -> {}", request.getId());
                logUtil.saveLog(request.getId(), request.getURI().toString(), true, null);
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Global Logging Filter end: response status -> {}", response.getStatusCode());
                    logUtil.saveLog(request.getId(), request.getURI().toString(), false, response.getStatusCode().toString());
                }
            }));
        });
    }


    @Data
    public static class Config {
        private boolean preLogger;
        private boolean postLogger;
    }
}
