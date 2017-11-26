package com.school.controllers.WebControllers;

import com.school.controllers.WebControllers.admin.AddMentorController;
import com.school.controllers.WebControllers.admin.AdminWebController;
import com.school.controllers.WebControllers.admin.EditMentorController;
import com.school.controllers.WebControllers.student.StudentQuestWebController;
import com.school.controllers.WebControllers.student.StudentWebController;
import com.school.controllers.WebControllers.utilController.Static;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;


public class App {

        public static void main(String[] args) throws Exception {
            // create a server on port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // set routes
            server.createContext("/loginForm", new LoginWebController());
            server.createContext("/students", new StudentWebController());
            server.createContext("/add_mentor", new AddMentorController());
            server.createContext("/edit_mentor", new EditMentorController());
            server.createContext("/admins", new AdminWebController());
            server.createContext("/artifacts", new ArtifactWebController());
            server.createContext("/quests", new StudentQuestWebController());
            server.createContext("/signUp", new SignUpWebController());
            server.createContext("/availablequests", new QuestWebController()) ;


            server.createContext("/static", new Static());
            server.setExecutor(null); // creates a default executor

            // start listening
            server.start();
        }
    }
