package ru.alex.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.alex.model.ResponseToken;
import ru.alex.model.User;
import ru.alex.service.UserService;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class RegistrationController extends Controller {

    private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(String request) {
        try {
            JsonParser jsonParser = objectMapper.getFactory().createParser(request);
            User user = objectMapper.readValue(jsonParser, User.class);
            String a = userService.save(user);
            System.out.println(a);
            return a;
        } catch (JsonProcessingException e) {
            return "HTTP/1.1 400 Bad Request\n" +
                    "Content-Type: application/json\r\n" +
                    "Date: " + Date.from(Instant.now()) + "\r\n" +
                    " { \n \" message\" : "+ "\"" + e.getMessage() + "\""+ " \n } \r\n" +
                    "\r\n";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
