package com.school.controllers.WebControllers;

import com.school.controllers.LoginController;
import com.school.models.User;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import java.net.URLDecoder;
import java.util.HashMap;


import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;


public class LoginForm implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {

            String userAgent = httpExchange.getRequestHeaders().getFirst("User-agent");

            JtwigTemplate template = JtwigTemplate.classpathTemplate("main_page.twig");

            JtwigModel model = JtwigModel.newModel();

            response = template.render(model);

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }

        if (method.equals("POST")) {

            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = parseFormData(formData);

            String firstName = inputs.get("login").toString();
            String lastName = inputs.get("password").toString();

            User user = LoginController.startLoginProcess(firstName, lastName);

            if (user == null) {
//                httpExchange.getResponseHeaders().add("Status", "Incore"

                JtwigTemplate template = JtwigTemplate.classpathTemplate("main_page.twig");

                JtwigModel model = JtwigModel.newModel();

                model.with("failed_login", true);
                response = template.render(model);

                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream os = httpExchange.getResponseBody();

                os.write(response.getBytes());
                os.close();


            } else if (user.getStatus().equals("student")) {

                httpExchange.getResponseHeaders().set("Location", "/students");

            } else if (user.getStatus().equals("mentor")) {

                httpExchange.getResponseHeaders().set("Location", "/mentors");

            } else if (user.getStatus().equals("admin")) {

                httpExchange.getResponseHeaders().set("Location", "/admins");
            }

            if (user != null) {
                String cookie = setUpCookies(user);
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie);
            }
                httpExchange.sendResponseHeaders(302, -1);
                OutputStream os = httpExchange.getResponseBody();

                os.write(response.getBytes());
                os.close();
            }
        }


    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private static String setUpCookies(User user) {

        OffsetDateTime oneHourFromNow = OffsetDateTime.now(ZoneOffset.UTC).plus(Duration.ofSeconds(60));
        String cookieExpire = "expires=" + DateTimeFormatter.RFC_1123_DATE_TIME.format(oneHourFromNow);
        String cookie = String.format("id=%s; %s;", user.getId(), cookieExpire);

        return cookie;
    }
}
