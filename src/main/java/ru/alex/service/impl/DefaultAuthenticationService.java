package ru.alex.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.model.ResponseToken;
import ru.alex.model.User;
import ru.alex.service.AuthenticationService;
import ru.alex.service.JwtService;
import ru.alex.service.UserService;
import ru.alex.util.SenderDefaultResponse;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class DefaultAuthenticationService implements AuthenticationService {

    private UserService userService;
    private JwtService jwtService;
    private ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(DefaultAuthenticationService.class);

    public DefaultAuthenticationService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String login(User user) {
        Optional<User> userOptional = userService.findByLogin(user.getLogin());
        if (userOptional.isPresent()) {
            if (userOptional.get().getPassword().equals(user.getPassword())) {
                ResponseToken token = jwtService.createTokens(user);
                try {
                    logger.info("User {} was login", user.getLogin());
                    return SenderDefaultResponse.sendOk(objectMapper.writeValueAsString(token));
                } catch (JsonProcessingException e) {
                    return SenderDefaultResponse.sendBadRequest();
                }
            }
        }
        logger.info("User {} was registered", user.getLogin());
        return null;
    }

    @Override
    public String registration(User user) throws JsonProcessingException {

        Optional<User> userOptional = userService.save(user);

        if (userOptional.isPresent()) {
            if (userService.findByLogin(user.getLogin()).isPresent()) {
                logger.info("User {} was registered", user.getLogin());
                return SenderDefaultResponse.sendConflict();
            }

            StringBuilder stringBuilder = new StringBuilder();
            ResponseToken token = jwtService.createTokens(user);

            stringBuilder.append("HTTP/1.1 201 Create\n");
            stringBuilder.append("Content-Type: application/json\r\n");
            stringBuilder.append("Date: ").append(Date.from(Instant.now())).append("\r\n").append("\r\n");
            stringBuilder.append(objectMapper.writeValueAsString(token)).append("\r\n");
            stringBuilder.append("\r\n");
            logger.info("User {} was registered", user.getLogin());
            return stringBuilder.toString();
        } else {
            return SenderDefaultResponse.sendBadRequest();
        }


    }
}
