package ru.alex.service.impl;

import ru.alex.model.ResponseToken;
import ru.alex.model.Token;
import ru.alex.model.User;
import ru.alex.security.jwt.factory.TokenFactory;
import ru.alex.security.jwt.serializer.TokenJwsStringSerializer;
import ru.alex.service.JwtService;

import java.util.UUID;

public class DefaultJwtService implements JwtService {

    private final TokenFactory accessFactory;
    private final TokenJwsStringSerializer tokenJwsStringSerializer;

    public DefaultJwtService(TokenFactory accessFactory, TokenJwsStringSerializer tokenJwsStringSerializer) {
        this.accessFactory = accessFactory;
        this.tokenJwsStringSerializer = tokenJwsStringSerializer;
    }


    public ResponseToken createTokens(User user) {
        Token token = accessFactory.apply(user);
        return new ResponseToken(tokenJwsStringSerializer.apply(token));
    }
}