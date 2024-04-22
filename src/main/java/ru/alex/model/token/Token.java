package ru.alex.model.token;

import java.time.Instant;

public record Token(String id, String subject, Instant createAt, Instant expireAt) {
}