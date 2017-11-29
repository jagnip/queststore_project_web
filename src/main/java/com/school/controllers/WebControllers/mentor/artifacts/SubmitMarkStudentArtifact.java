package com.school.controllers.WebControllers.mentor.artifacts;

import com.school.controllers.WebControllers.mentor.MentorSessionController;
import com.school.dao.ArtefactDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

public class SubmitMarkStudentArtifact extends MentorSessionController implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();

     if (method.equals("POST")) {

            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = parseFormData(formData);

            Integer artifact_id = Integer.parseInt(inputs.get("artifact_id").toString());
            Integer student_id = Integer.parseInt(inputs.get("student_id").toString());

            ArtefactDAO artefactDAO = new ArtefactDAO();
            artefactDAO.deleteStudentArtifact(artifact_id, student_id);

            JtwigTemplate template = JtwigTemplate.classpathTemplate("/static/MentorTemplates/manageartifacts.html");
            JtwigModel model = JtwigModel.newModel();
            model.with("artifact_marked", true);
            model.with("artifacts", artefactDAO.getAllArtifacts());


            response = template.render(model);
        }

        final byte[] finalResponseBytes = response.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(200, finalResponseBytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}


