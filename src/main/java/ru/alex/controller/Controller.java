package ru.alex.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Controller {

    protected ObjectMapper objectMapper = new ObjectMapper();


    public abstract String execute(String request) throws JsonProcessingException;
}
