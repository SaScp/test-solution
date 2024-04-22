package ru.alex.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParserRequest {

    public static String getBodyFromRequest(BufferedReader reader) throws IOException {
        StringBuilder payload = new StringBuilder();
        while (reader.ready()) {
            payload.append((char) reader.read());
        }
        return payload.toString();
    }

    public static Map<String, String> getHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while (!(headerLine = reader.readLine()).isEmpty()) {
            String[] header = headerLine.split(": ");
            headers.put(header[0], header[1]);
        }

        return headers;
    }

    public static String[] parseUrlFromRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        String[] requestParts = requestLine.split(" ");
        String url = requestParts[1];

        System.out.println(url);
        if (url.contains(" ")) {
            url = url.split(" ")[0];
        }
        return new String[]{requestParts[0], url};
    }
}
