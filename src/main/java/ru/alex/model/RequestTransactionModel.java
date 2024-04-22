package ru.alex.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestTransactionModel {

    @JsonProperty("to")
    private String userTo;
    @JsonProperty("amount")
    private Double amount;

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
