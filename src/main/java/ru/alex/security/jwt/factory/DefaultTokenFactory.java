package ru.alex.security.jwt.factory;

;

import ru.alex.model.Token;
import ru.alex.model.User;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


public class DefaultTokenFactory implements TokenFactory {

    private Duration duration = Duration.ofMinutes(30);

    @Override
    public Token apply(User user) {
        var now = Instant.now();
        return new Token(UUID.randomUUID().toString(), user.getLogin(), now, now.plus(duration));
    }

}
