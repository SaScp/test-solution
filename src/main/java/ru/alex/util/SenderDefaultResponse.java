package ru.alex.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class SenderDefaultResponse {

    public static String sendBalance(double balance) {
            return "HTTP/1.1 200 OK\n" +
                    "Content-Type: application/json\r\n" +
                    "Date: " + Date.from(Instant.now()) + "\r\n\r\n" +
                    "{\n" +
                    " \"balance\": \""+ balance +"\" \n" +
                    "}";
    }

    public static String sendNotFound() {
        return "HTTP/1.1 404 Not Found\n" +
                "Content-Type: application/json\r\n" +
                "Date: " + Date.from(Instant.now()) + "\r\n\r\n" +
                "{\n" +
                " \"message\": \"page not found\" \n" +
                "}";
    }

    public static String sendUnauthorized()  {
        return "HTTP/1.1 401 UNAUTHORIZED\n" +
                "Content-Type: application/json\r\n" +
                "Date: " + Date.from(Instant.now()) + "\r\n\r\n" +
                "{\n" +
                " \"message\": \"UNAUTHORIZED\" \n" +
                "}";
    }

    public static String sendConflict() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 409 Conflict\n");
        stringBuilder.append("Content-Type: application/json\r\n");
        stringBuilder.append("Date: ").append(Date.from(Instant.now())).append("\r\n").append("\r\n");
        stringBuilder.append(" { \n \"message\" : " + "\"" + "user already exists" + "\"" + " \n } \r\n");
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }

    public static String sendBadRequest()  {
        return "HTTP/1.1 400 Bad Request\n" +
                "Content-Type: application/json\r\n" +
                "Date: " + Date.from(Instant.now()) + "\r\n\r\n" +
                "{\n" +
                " \"message\": \"bad request\" \n" +
                "}";
    }

    public static String sendOk() {
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: application/json\r\n" +
                "Date: " + Date.from(Instant.now()) + "\r\n\r\n" ;
    }

    public static String sendOk(String jsonValue) {
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: application/json\r\n" +
                "Date: " + Date.from(Instant.now()) + "\r\n\r\n" +
                jsonValue;
    }
    public static String sendCreate(String jsonValue) {
        return "HTTP/1.1 201 Create\\n" +
                "Content-Type: application/json\r\n" +
                "Date: " + Date.from(Instant.now()) + "\r\n\r\n" +
                jsonValue;
    }
}
