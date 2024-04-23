package ru.alex.dispatcher;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.controller.AuthController;
import ru.alex.controller.LoginController;
import ru.alex.controller.MoneyController;
import ru.alex.controller.RegistrationController;

import ru.alex.model.User;
import ru.alex.repository.UserRepository;
import ru.alex.security.filter.JwtFilter;
import ru.alex.security.jwt.deserializer.DefaultTokenJwsStringDeserializer;
import ru.alex.security.jwt.factory.DefaultTokenFactory;
import ru.alex.security.jwt.serializer.DefaultTokenJwsStringSerializer;
import ru.alex.service.AuthenticationService;
import ru.alex.service.UserService;
import ru.alex.service.impl.DefaultAuthenticationService;
import ru.alex.service.impl.DefaultJwtService;
import ru.alex.service.impl.DefaultUserService;
import ru.alex.util.ParserRequest;
import ru.alex.util.SenderDefaultResponse;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class Dispatcher extends Thread {

    private Map<String, AuthController> authEndpoints;
    private Set<String> endpoints;

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private JwtFilter jwtFilter;
    private final String token = "{\"kty\" : \"oct\", \"k\" : \"hi7S5RX5ZRZooHA0RKGctZ-KtR9FoESgCnH-3BNg5XI\"}";

    private MoneyController moneyController;

    public Dispatcher(Socket socket) throws IOException,
            ParseException, JOSEException, SQLException, ClassNotFoundException {

        UserService userService = new DefaultUserService(new UserRepository(), new DefaultJwtService(new DefaultTokenFactory(),
                new DefaultTokenJwsStringSerializer(new MACSigner(OctetSequenceKey.parse(token)))));
        AuthenticationService authenticationService = new DefaultAuthenticationService(userService, new DefaultJwtService(new DefaultTokenFactory(),
                new DefaultTokenJwsStringSerializer(new MACSigner(OctetSequenceKey.parse(token)))));
        this.jwtFilter = new JwtFilter(new DefaultTokenJwsStringDeserializer(
                new MACVerifier(OctetSequenceKey.parse(token))
        ), userService);


        socketInit(socket);
        endPointInit(authenticationService);

        this.moneyController = new MoneyController(userService);
    }

    private void endPointInit(AuthenticationService authenticationService) {
        this.authEndpoints = new HashMap<>();
        this.authEndpoints.put("/signup", new RegistrationController(authenticationService));
        this.authEndpoints.put("/signin", new LoginController(authenticationService));

        this.endpoints = new HashSet<>();
        this.endpoints.add("/money");
    }

    private void socketInit(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                handler();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handler() throws IOException {
        String[] request = ParserRequest.parseUrlFromRequest(reader);
        String method = request[0];
        String endpoint = request[1];

        Optional<User> user = jwtFilter.doFilter(ParserRequest.getHeaders(reader));
        String requestBody = ParserRequest.getBodyFromRequest(reader);

        if (Optional.ofNullable(authEndpoints.get(endpoint)).isEmpty() && !endpoints.contains(endpoint)) {
            writer.write(SenderDefaultResponse.sendNotFound());
        } else if (user.isEmpty() && !endpoint.equals("/signup") && !endpoint.equals("/signin")) {
            writer.write(SenderDefaultResponse.sendUnauthorized());
        } else {
            roting(endpoint, method, requestBody, user);
        }
    }

    private void roting(String endpoint, String method, String requestBody, Optional<User> user) throws IOException {
        AuthController controller = authEndpoints.get(endpoint);

        if (method.equals("POST") && endpoint.equals("/signup") || endpoint.equals("/signin")) {
            writer.write(controller.execute(requestBody));
        } else {
            moneyRouting(endpoint, method, requestBody, user);
        }
        writer.flush();
    }

    private void moneyRouting(String endpoint, String method, String requestBody, Optional<User> user) throws IOException {
        if (method.equals("GET") && endpoint.equals("/money")) {
            writer.write(SenderDefaultResponse.sendBalance(user.get().getBalance()));
            logger.info("User {} balance is {}$",
                    user.get().getLogin(), user.get().getBalance());

        } else if (method.equals("POST") && endpoint.equals("/money")) {
            if (moneyController.execute(requestBody, user.get().getLogin())) {
                writer.write(SenderDefaultResponse.sendOk());
            } else {
                writer.write(SenderDefaultResponse.sendBadRequest());
            }
        }
    }
}
