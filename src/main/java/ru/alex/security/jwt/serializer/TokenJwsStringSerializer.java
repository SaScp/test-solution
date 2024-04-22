package ru.alex.security.jwt.serializer;




import ru.alex.model.token.Token;

import java.util.function.Function;

public interface TokenJwsStringSerializer extends Function<Token, String> {
}
