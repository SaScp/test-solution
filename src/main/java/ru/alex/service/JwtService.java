package ru.alex.service;

import ru.alex.model.token.ResponseToken;
import ru.alex.model.User;

public interface JwtService {
    public ResponseToken createTokens(User user);
}
