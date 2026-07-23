package ru.darujo.reader;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ReactiveHttpInputMessage;

import java.io.InputStream;

public class HttpInputMessageImp implements HttpInputMessage {
    private final ReactiveHttpInputMessage message;
    private final InputStream inputStream;

    public HttpInputMessageImp(ReactiveHttpInputMessage message, InputStream inputStream) {
        this.message = message;
        this.inputStream = inputStream;
    }

    @Override
    public @NonNull InputStream getBody() {
        return inputStream;
    }

    @Override
    public @NonNull HttpHeaders getHeaders() {
        return message.getHeaders();
    }
}
