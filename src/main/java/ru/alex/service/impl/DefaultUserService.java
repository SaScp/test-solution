package ru.alex.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.alex.model.ResponseToken;
import ru.alex.model.Token;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;
import ru.alex.service.JwtService;
import ru.alex.service.UserService;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class DefaultUserService implements UserService {

    private UserRepository userRepository;
    private JwtService jwtService;
    private ObjectMapper objectMapper;

    public DefaultUserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.objectMapper = new ObjectMapper();
    }


    public Optional<User> findByLogin(String login) {

        return Optional.empty();
    }

    public String save(User user) throws JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder();
        if (findByLogin(user.getLogin()).isEmpty()) {
            if (userRepository.save(user)) {
                ResponseToken token = jwtService.createTokens(user);
                stringBuilder.append("HTTP/1.1 201 Create\n");
                stringBuilder.append("Content-Type: application/json\r\n");
                stringBuilder.append("Date: ").append(Date.from(Instant.now())).append("\r\n").append("\r\n");
                stringBuilder.append(objectMapper.writeValueAsString(token)).append("\r\n");
                stringBuilder.append("\r\n");
                return stringBuilder.toString();
            }
        }
        return sendConflict();
    }
    private String sendConflict() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 409 Conflict\n");
        stringBuilder.append("Content-Type: application/json\r\n");
        stringBuilder.append("Date: ").append(Date.from(Instant.now())).append("\r\n").append("\r\n");
        stringBuilder.append(" { \n \"message\" : "+ "\"" + "user already exists" + "\""+ " \n } \r\n");
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }
}
