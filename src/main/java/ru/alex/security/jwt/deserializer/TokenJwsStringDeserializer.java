package ru.alex.security.jwt.deserializer;





import ru.alex.model.Token;

import java.util.function.Function;

public interface TokenJwsStringDeserializer extends Function<String, Token> {
}
