package com.school.controllers.WebControllers.admin;

import com.school.dao.MentorDAO;
import com.school.models.Admin;
import com.school.models.Mentor;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;


import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


public class AddMentorController extends AdminSessionController implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        Headers requestHeaders = httpExchange.getRequestHeaders();
        Integer userID = getIdFromExistingCookies(requestHeaders);

        if (userID == null) {

            httpExchange.getResponseHeaders().set("Location", "/loginForm");
            httpExchange.sendResponseHeaders(302, response.length());

        } else if (method.equals("GET")) {

            Admin admin = loadAdmin(userID);

            if (admin != null) {
                String cookie = setupCookies(admin);
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie);
            }

            JtwigTemplate template = JtwigTemplate.classpathTemplate("/static/AdminTemplates/addmentor.html");

            JtwigModel model = JtwigModel.newModel();
            response = template.render(model);

        } else if (method.equals("POST")) {

            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = parseFormData(formData);

            String firstName = inputs.get("first_name").toString();
            String lastName = inputs.get("last_name").toString();
            String email = inputs.get("email").toString();
            String course = inputs.get("class").toString();
            String password = inputs.get("password").toString();

            Mentor mentor = new Mentor(firstName, lastName, password, email);
            MentorDAO mentorDAO = new MentorDAO(mentor);
            mentorDAO.save();

            JtwigTemplate template = JtwigTemplate.classpathTemplate("/static/AdminTemplates/admin_account.html");

            JtwigModel model = JtwigModel.newModel();
            model.with("mentor_added", true);

            response = template.render(model);

        }


        final byte[] finalResponseBytes = response.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(200, finalResponseBytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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
}
