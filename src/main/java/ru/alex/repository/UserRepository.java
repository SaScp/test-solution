package ru.alex.repository;

import ru.alex.SQLConfig;
import ru.alex.model.User;

import java.sql.*;
import java.util.Optional;

public class UserRepository {

    public Connection connection;
    public UserRepository() throws ClassNotFoundException, SQLException {
        this.connection = new SQLConfig().getConnection();

    }
    public boolean save(User user) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO t_user (id, login, password, balance) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setDouble(4, user.getBalance());
            preparedStatement.execute();
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public Optional<User> findByLogin(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM t_user WHERE login = ? LIMIT 1")) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getDouble("balance")));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
    public boolean update(User user) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE t_user SET balance = ? WHERE id = ?")) {
            preparedStatement.setDouble(1, user.getBalance());
            preparedStatement.setString(2, user.getId());
            preparedStatement.execute();
        }catch (SQLException e){
            return false;
        }
        return true;
    }

}
