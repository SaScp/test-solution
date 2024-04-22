package ru.alex.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.alex.model.User;

public interface AuthenticationService {
    public String login(User user);
    public String registration(User user) throws JsonProcessingException;
}
