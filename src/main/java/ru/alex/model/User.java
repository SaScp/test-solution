package ru.alex.model;

public class User {

    private String id;

    private String login;

    private String password;

    private Double balance;

    public User() {
    }
    public User(String id, String login, String password, Double balance) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.balance = balance;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
