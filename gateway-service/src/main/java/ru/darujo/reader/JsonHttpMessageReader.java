package ru.darujo.reader;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tools.jackson.databind.JsonNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class JsonHttpMessageReader implements HttpMessageReader<JsonNode> {
    JacksonJsonHttpMessageConverter jc = new JacksonJsonHttpMessageConverter();

    @Override
    public @NonNull List<MediaType> getReadableMediaTypes() {
        return List.of(MediaType.APPLICATION_JSON);
    }

    @Override
    public boolean canRead(@NonNull ResolvableType elementType, @Nullable MediaType mediaType) {

        return jc.canRead(elementType, mediaType);
    }

    @Override
    public @NonNull Flux<JsonNode> read(@NonNull final ResolvableType elementType, @NonNull final ReactiveHttpInputMessage message,
                                        @NonNull final Map<String, Object> hints) {
        return Flux.merge(readMono(elementType, message, hints));
    }

    @Override
    public @NonNull Mono<JsonNode> readMono(@NonNull final ResolvableType elementType, @NonNull final ReactiveHttpInputMessage message,
                                            @NonNull final Map<String, Object> hints) {
        return Mono.from(readMultipartData(message));
    }

    private JsonNode readMultipartData(ReactiveHttpInputMessage message, InputStream inputStream) {
        HttpInputMessage inputMessage = new HttpInputMessageImp(message, inputStream);
        try {
            Object o = jc.read(JsonNode.class, inputMessage);
            return (JsonNode) o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Mono<JsonNode> readMultipartData(final ReactiveHttpInputMessage message) {
        log.warn("readMultipartData");
        return (message.getBody()
                .subscribeOn(Schedulers.boundedElastic())
                .map(dataBuffer -> concat(dataBuffer.readableByteCount(), dataBuffer.readableByteBuffers())))
                .collectList().map(byteList -> {
                    ByteBuffer bb = concat(byteList);
                    byte[] bytes = bb.array();
                    return readMultipartData(message, new ByteArrayInputStream(bytes));
                });
    }

    public static ByteBuffer concat(List<ByteBuffer> buffers) {
        // Считаем общий размер
        AtomicInteger totalSize = new AtomicInteger();
        buffers.forEach(buffer -> {
            if (buffer == null) {
                throw new IllegalArgumentException("Buffer cannot be null");
            }
            buffer.rewind(); // Обязательно сбрасываем позицию
            totalSize.addAndGet(buffer.remaining());
        });
        return concat(totalSize.get(), buffers.iterator());
    }

    public static ByteBuffer concat(int totalSize, Iterator<ByteBuffer> buffers) {
        // Выделяем новый буфер и копируем данные
        ByteBuffer combined = ByteBuffer.allocate(totalSize);
        buffers.forEachRemaining(buffer -> {
                    if (buffer == null) {
                        throw new IllegalArgumentException("Buffer cannot be null");
                    }
                    buffer.rewind();
                    combined.put(buffer);
                }
        );

        // Фиксируем состояние буфера
        combined.rewind();
        return combined;
    }
}
