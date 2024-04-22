package ru.alex.model;

import java.time.Instant;
import java.util.List;

public record Token(String id, String subject, Instant createAt, Instant expireAt) {
}