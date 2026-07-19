package ru.darujo.filter;

import org.jspecify.annotations.NonNull;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.http.codec.CodecCustomizer;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.MessageBodyDecoder;
import org.springframework.cloud.gateway.filter.factory.rewrite.MessageBodyEncoder;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Set;


@Component
public class ReplaceResponseGatewayFilterFactory extends ModifyResponseBodyGatewayFilterFactory {

    @Autowired
    public ReplaceResponseGatewayFilterFactory(List<HttpMessageReader<?>> messageReaders, Set<MessageBodyDecoder> messageBodyDecoders, Set<MessageBodyEncoder> messageBodyEncoders) {
        super(messageReaders, messageBodyDecoders, messageBodyEncoders);
    }

    public ReplaceResponseGatewayFilterFactory(List<HttpMessageReader<?>> messageReaders, Set<MessageBodyDecoder> messageBodyDecoders, Set<MessageBodyEncoder> messageBodyEncoders, List<CodecCustomizer> codecCustomizers) {
        super(messageReaders, messageBodyDecoders, messageBodyEncoders, codecCustomizers);
    }

    @Override
    public @NonNull GatewayFilter apply(@NonNull Config config) {

        config.setRewriteFunction(JsonNode.class, JsonNode.class, new ReplacerHref());

//        return super
//                .apply(config);
        return new MyModifyResponseGatewayFilter(config);
    }

    public class MyModifyResponseGatewayFilter extends ModifyResponseGatewayFilter {
        MyModifyResponseGatewayFilter(Config config) {
            super(config);
        }

        @Override
        public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpResponse serverHttpResponse = getServerHttpResponseFromSuper(exchange);
            ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
                @Override
                public @NonNull Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(getDelegate().getHeaders().getContentType())) {
                        return serverHttpResponse.writeWith(body);
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(responseDecorator).build());
        }

        private ServerHttpResponse getServerHttpResponseFromSuper(ServerWebExchange exchange) {
            final ServerHttpResponse[] serverHttpResponse = new ServerHttpResponse[1];
            //noinspection UnassignedFluxMonoInstance
            super.filter(exchange, chain -> {
                serverHttpResponse[0] = chain.getResponse(); // capture the response when the super sets it
                return Mono.empty();
            });
            return serverHttpResponse[0];
        }
    }

}
