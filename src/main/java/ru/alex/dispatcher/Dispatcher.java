package ru.alex.dispatcher;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import ru.alex.controller.Controller;
import ru.alex.controller.LoginController;
import ru.alex.controller.MoneyController;
import ru.alex.controller.RegistrationController;

import ru.alex.repository.UserRepository;
import ru.alex.security.filter.JwtFilter;
import ru.alex.security.jwt.deserializer.DefaultTokenJwsStringDeserializer;
import ru.alex.security.jwt.factory.DefaultTokenFactory;
import ru.alex.security.jwt.serializer.DefaultTokenJwsStringSerializer;
import ru.alex.service.UserService;
import ru.alex.service.impl.DefaultJwtService;
import ru.alex.service.impl.DefaultUserService;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;

public class Dispatcher extends Thread {

    private Map<String, Controller> endpoints;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private JwtFilter jwtFilter;
    private final String token = "{\"kty\" : \"oct\", \"k\" : \"hi7S5RX5ZRZooHA0RKGctZ-KtR9FoESgCnH-3BNg5XI\"}";

    public Dispatcher(Socket socket) throws IOException, ParseException, JOSEException {

        UserService userService = new DefaultUserService(new UserRepository(), new DefaultJwtService(new DefaultTokenFactory(),
                new DefaultTokenJwsStringSerializer(new MACSigner(OctetSequenceKey.parse(token)))));

        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        this.jwtFilter = new JwtFilter(new DefaultTokenFactory(),
                new DefaultTokenJwsStringDeserializer(
                        new MACVerifier(OctetSequenceKey.parse(token))
                ), userService);

        this.endpoints = new HashMap<>();
        this.endpoints.put("/signup", new RegistrationController(userService));
        this.endpoints.put("/signin", new LoginController());
        this.endpoints.put("/money", new MoneyController());
    }


    @Override
    public void run() {
        while (true) {
            try {
                handler();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void handler() throws IOException {
        String endpoint = parseUrlFromRequest(reader);
        var a = getHeaders(reader);
        System.out.println(a);
        if (Optional.ofNullable(endpoints.get(endpoint)).isEmpty()) {
            sendNotFound();
        } else if (jwtFilter.doFilter(a).isEmpty() && !endpoint.equals("/signup") && !endpoint.equals("/signin")){
            sendUnauthorized();
        } else {
            PrintWriter writer1 = new PrintWriter(socket.getOutputStream(), true);
            Controller controller = endpoints.get(endpoint);
            writer1.write(controller.execute(getBodyFromRequest(reader)));
            writer1.flush();
        }

    }

    private void sendNotFound() throws IOException {
        writer.write("HTTP/1.1 404 Not found\n");
        writer.write("Content-Type: application/json\r\n");
        writer.write("Date: " + Date.from(Instant.now()) + "\r\n");
        writer.write("\r\n");
        writer.write("{ \n \"message\" : "+ "\"" + "page not found" + "\"" + " \n } \r\n");
        writer.flush();
    }

    private void sendUnauthorized() throws IOException {
        writer.write("HTTP/1.1 401 UNAUTHORIZED\n");
        writer.write("Content-Type: application/json\r\n");
        writer.write("Date: " + Date.from(Instant.now()) + "\r\n");
        writer.write("\r\n");
        writer.flush();
    }

    private String getBodyFromRequest(BufferedReader reader) throws IOException {

        StringBuilder payload = new StringBuilder();
        while (reader.ready()) {
            payload.append((char) reader.read());
        }
        return payload.toString();
    }

    private Map<String, String> getHeaders(BufferedReader reader) throws IOException {

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while (!(headerLine = reader.readLine()).isEmpty()) {
            String[] header = headerLine.split(": ");
            headers.put(header[0], header[1]);
        }

        return headers;
    }

    private String parseUrlFromRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        String[] requestParts = requestLine.split(" ");
        String url = requestParts[1];

        System.out.println(url);
        if (url.contains(" ")) {
            url = url.split(" ")[0];
        }
        return url;
    }
}
