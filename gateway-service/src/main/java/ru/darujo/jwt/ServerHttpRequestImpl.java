package ru.darujo.jwt;

import org.apache.http.client.utils.URIBuilder;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;

public class ServerHttpRequestImpl extends ServerHttpRequestDecorator {

    // your own query params implementation
    private final MultiValueMap<String, String> queryParams;

    public ServerHttpRequestImpl(ServerHttpRequest request) {
        super(request);
        this.queryParams = new LinkedMultiValueMap<>();
        request.getQueryParams().forEach((s, strings) -> {
            if (!s.startsWith("system_")) {
                this.queryParams.addAll(s, strings);
            }
        });


    }

    @Override
    @NullMarked
    public MultiValueMap<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    @NullMarked
    public URI getURI() {
        String url = getDelegate().getURI().toString();
        if (url.indexOf("?") > 0) {
            url = url.substring(0, url.indexOf("?"));
        }
        try {
            URIBuilder builder = new URIBuilder(url);
            queryParams.forEach((teg, valueList) ->
                    valueList.forEach(value -> builder.addParameter(teg, value)));
            return builder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
