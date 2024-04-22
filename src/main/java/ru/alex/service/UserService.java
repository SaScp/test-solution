package ru.alex.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.alex.model.transaction.TransactionModel;
import ru.alex.model.User;

import java.util.Optional;

public interface UserService {
    public Optional<User> findByLogin(String login);

    public Optional<User> save(User user) throws JsonProcessingException;

    public boolean sendMoney(TransactionModel transactionModel);

}
