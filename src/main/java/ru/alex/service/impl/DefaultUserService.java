package ru.alex.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.model.transaction.TransactionModel;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;
import ru.alex.service.JwtService;
import ru.alex.service.UserService;

import java.util.Optional;
import java.util.UUID;

public class DefaultUserService implements UserService {

    private UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(DefaultUserService.class);

    public DefaultUserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
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
                logger.info("User {} was send {}$ to the user {}",
                        currentUserFrom.getLogin(), transactionModel.getAmount(), currentUserTo.getLogin());
                return true;
            }
        }
        return false;

    }

    public Optional<User> save(User user) {
            user.setId(UUID.randomUUID().toString());
            user.setBalance(0.0);
            if (userRepository.save(user)) {
                return Optional.ofNullable(user);
            }
        return Optional.empty();
    }

}
