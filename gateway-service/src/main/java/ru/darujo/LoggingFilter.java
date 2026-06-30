package ru.darujo;

import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {

//    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public @NullMarked Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        logger.info("Request path: {}", exchange.getRequest().getPath());
        System.out.printf("Request path: %s %s \n", exchange.getRequest().getPath(), exchange.getRequest().getQueryParams());
        return chain.filter(exchange);
    }
}