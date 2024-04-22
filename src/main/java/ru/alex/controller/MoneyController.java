package ru.alex.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.alex.model.RequestTransactionModel;
import ru.alex.model.TransactionModel;
import ru.alex.service.UserService;

import java.io.IOException;

public class MoneyController {

    private final UserService userService;

    private ObjectMapper objectMapper;

    public MoneyController(UserService userService) {
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    public boolean execute(String request, String userFrom) throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(request);
        RequestTransactionModel requestTransactionModel = objectMapper.readValue(jsonParser, RequestTransactionModel.class);
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setUserTo(requestTransactionModel.getUserTo());
        transactionModel.setAmount(requestTransactionModel.getAmount());
        transactionModel.setUserFrom(userFrom);
        return userService.sendMoney(transactionModel);
    }
}
