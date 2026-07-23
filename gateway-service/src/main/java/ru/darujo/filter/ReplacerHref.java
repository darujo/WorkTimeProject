package ru.darujo.filter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;


public class ReplacerHref implements RewriteFunction<JsonNode, JsonNode> {
    // ... fields and constructor omitted
    @Override
    public @NonNull Publisher<JsonNode> apply(@Nullable ServerWebExchange t, JsonNode u) {
        return Mono.just(scrubRecursively(u));
//    return Mono.empty();
    }

    private JsonNode scrubRecursively(JsonNode u) {
        if (!u.isContainer()) {
            return u;
        }

        if (u.isObject()) {


            u.values().forEach((f) -> {


                if (f.isObject()) {
                    for (Map.Entry<String, JsonNode> entry : f.properties()) {
                        if (entry.getKey().equalsIgnoreCase("href") && entry.getValue().isString()) {
                            ObjectNode node = (ObjectNode) f;
                            node.put(entry.getKey(), replaceHref(entry.getValue().stringValue()));
                        } else {
                            if (!f.isValueNode()) {
                                scrubRecursively(f);
                            }
                        }
                    }

                } else {
                    if (f.isValueNode()) {
                        scrubRecursively(f);
                    }
                }
            });
        } else if (u.isArray()) {
            ArrayNode array = (ArrayNode) u;
            for (int i = 0; i < array.size(); i++) {
                array.set(i, scrubRecursively(array.get(i)));
            }
        }

        return u;
    }

    private static Map<String, String> mapReplace;

    private String replaceHref(String s) {
        for (String chatIdOld : mapReplace.keySet()) {
            s = s.replace(chatIdOld, mapReplace.get(chatIdOld));
        }

        if (s.indexOf("?") > 0) {
            String paramStr = s.substring(s.indexOf("?"));
//            if (params.length > 1) {
            String[] params = paramStr.split("&");
            for (String param : params) {
                if (param.startsWith("system")) {
                    s = s.replace(param, "");
                }
            }
            while (s.indexOf("&&") > 0) {
                s = s.replace("&&", "&");
            }
            s = s.replace("?&", "&");
//            }
        }
        return s;
    }

    private void init() {
        if (mapReplace == null) {
            mapReplace = new HashMap<>();
            mapReplace.put("http://localhost:8182/app", "https://sheduler.ru/work-service");
        }
    }

    public ReplacerHref() {
        init();
    }
}
