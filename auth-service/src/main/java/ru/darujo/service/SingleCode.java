package ru.darujo.service;

import org.jspecify.annotations.Nullable;

import java.sql.Timestamp;

public record SingleCode(String login, Long projectId, @Nullable String messageType, Timestamp timestamp) {
}
