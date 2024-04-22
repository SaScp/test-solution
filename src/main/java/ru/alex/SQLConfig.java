package ru.alex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.postgresql.Driver;
public class SQLConfig {

    public static final String URL = "jdbc:postgresql://localhost:5432/testcase";
    public static final String USER = "";
    public static final String PASSWORD = "";
    public static final String DRIVER = "org.postgresql.Driver";
    public Connection connection;

    public SQLConfig() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
