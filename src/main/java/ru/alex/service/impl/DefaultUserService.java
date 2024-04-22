package ru.alex.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.alex.model.ResponseToken;
import ru.alex.model.TransactionModel;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;
import ru.alex.service.JwtService;
import ru.alex.service.UserService;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class DefaultUserService implements UserService {

    private UserRepository userRepository;
    private JwtService jwtService;
    private ObjectMapper objectMapper;

    public DefaultUserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.objectMapper = new ObjectMapper();
    }


    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    public boolean sendMoney(TransactionModel transactionModel) {
        Optional<User> userFromOptional = userRepository.findByLogin(transactionModel.getUserFrom());
        Optional<User> userToOptional = userRepository.findByLogin(transactionModel.getUserTo());

        if (userFromOptional.isPresent() && userToOptional.isPresent()) {
            User currentUserFrom = userFromOptional.get();
            User currentUserTo = userToOptional.get();

            if (currentUserFrom.getBalance() >= transactionModel.getAmount()) {
                currentUserFrom.setBalance(currentUserFrom.getBalance() - transactionModel.getAmount());
                currentUserTo.setBalance(currentUserTo.getBalance() + transactionModel.getAmount());
                userRepository.update(currentUserFrom);
                userRepository.update(currentUserTo);
                return true;
            }
        }
        return false;

    }

    public Optional<User> save(User user) throws JsonProcessingException {
            user.setId(UUID.randomUUID().toString());
            user.setBalance(0.0);
            if (userRepository.save(user)) {
                return Optional.ofNullable(user);
            }
        return Optional.empty();
    }

}
