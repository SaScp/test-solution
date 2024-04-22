package ru.alex.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.alex.model.User;
import ru.alex.service.AuthenticationService;
import ru.alex.service.UserService;
import ru.alex.util.SenderDefaultResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class RegistrationController extends AuthController {

    private AuthenticationService userService;

    public RegistrationController(AuthenticationService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(String request)  {
        try {
            JsonParser jsonParser = objectMapper.getFactory().createParser(request);
            User user = objectMapper.readValue(jsonParser, User.class);
            return userService.registration(user);
        } catch (JsonProcessingException e) {
            return SenderDefaultResponse.sendBadRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
