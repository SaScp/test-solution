package ru.alex.security.jwt.factory;



import ru.alex.model.token.Token;
import ru.alex.model.User;

import java.util.function.Function;

public interface TokenFactory extends Function<User, Token> {
}
