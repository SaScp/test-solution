package ru.alex.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.alex.model.ResponseToken;
import ru.alex.model.User;
import ru.alex.service.AuthenticationService;
import ru.alex.service.UserService;
import ru.alex.util.SenderDefaultResponse;

import java.io.IOException;

public class LoginController extends AuthController {


    private AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public String execute(String request)  {
        try {
            JsonParser jsonParser = objectMapper.getFactory().createParser(request);
            User user = objectMapper.readValue(jsonParser, User.class);
            return authenticationService.login(user);
        } catch (JsonProcessingException e) {
            return SenderDefaultResponse.sendBadRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
