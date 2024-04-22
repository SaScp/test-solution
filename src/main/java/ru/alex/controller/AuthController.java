package ru.alex.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class AuthController {

    protected ObjectMapper objectMapper = new ObjectMapper();


    public abstract String execute(String request) throws IOException;
}
