package ru.alex;

import com.nimbusds.jose.JOSEException;
import ru.alex.dispatcher.Dispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {
    private static ExecutorService dispatchers = Executors.newFixedThreadPool(10);
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    dispatchers.submit(new Dispatcher(socket));
                } catch (IOException e) {
                    socket.close();
                } catch (ParseException | ClassNotFoundException | SQLException | JOSEException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            server.close();
        }
    }

}