package ru.alex.security.filter;

import ru.alex.model.Token;
import ru.alex.model.User;
import ru.alex.security.jwt.deserializer.TokenJwsStringDeserializer;
import ru.alex.security.jwt.factory.TokenFactory;
import ru.alex.service.UserService;
import ru.alex.service.impl.DefaultUserService;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class JwtFilter {

    private final TokenFactory tokenFactory;
    private final TokenJwsStringDeserializer tokenJwsStringDeserializer;
    private final UserService userService;

    public JwtFilter(TokenFactory tokenFactory,
                     TokenJwsStringDeserializer tokenJwsStringDeserializer,
                     UserService userService) {
        this.tokenFactory = tokenFactory;
        this.tokenJwsStringDeserializer = tokenJwsStringDeserializer;
        this.userService = userService;
    }

    public Optional<User> doFilter(Map<String, String> headers) {

        if (Optional.ofNullable(headers.get("Authorization")).isEmpty()) {
            return Optional.empty();
        } else if (!headers.get("Authorization").startsWith("Bearer ")) {
            return Optional.empty();
        }

        String token = headers.get("Authorization").replace("Bearer ", "");
        Token currentToken = tokenJwsStringDeserializer.apply(token);

        if (currentToken.expireAt() == Instant.now()) {
            return Optional.empty();
        }
        return userService.findByLogin(currentToken.subject());
    }
}
