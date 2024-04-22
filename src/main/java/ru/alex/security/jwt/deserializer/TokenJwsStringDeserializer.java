package ru.alex.security.jwt.deserializer;





import ru.alex.model.token.Token;

import java.util.function.Function;

public interface TokenJwsStringDeserializer extends Function<String, Token> {
}
