
package com.API_Gateway.APIGateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter
        extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger log =
            LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().name();

            log.info("Incoming request: {} {}", method, path);

            return chain.filter(exchange)
                    .doOnSuccess(result ->
                            log.info("Request completed: {}", path))
                    .doOnError(error ->
                            log.error("Request failed: {} - {}",
                                    path, error.getMessage()));
        };
    }

    public static class Config {
    }
}
