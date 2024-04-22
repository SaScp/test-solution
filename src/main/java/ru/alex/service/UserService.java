package ru.alex.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.alex.model.User;

import java.util.Optional;

public interface UserService {
    public Optional<User> findByLogin(String login);
    public String save(User user) throws JsonProcessingException;
}
