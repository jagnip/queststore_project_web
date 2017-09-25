package com.school.controllers;


import com.school.dao.CourseDAO;
import com.school.dao.DaoMentor;
import com.school.dao.DaoStudent;
import com.school.dao.UserDao;
import com.school.models.Course;
import com.school.models.Student;
import com.school.models.User;
import com.school.views.MentorView;

import java.util.ArrayList;

public class MentorController {

    public static void startController(User mentor){

        DaoMentor myMentor = new DaoMentor(mentor);
        //myMentor.loadAllAttributes();

        MentorView.welcomeMsg(mentor.getName());
        MentorView.showMenu();

        String choice = MentorView.getChoice();
        startRequestProcess(choice);
    }

    public static void startRequestProcess(String choice){

        if(choice.equals("1")){
            getNewStudentInfo();

        } else if(choice.equals("2")){
            showCourseInfo();

        } else if(choice.equals("3")){
            manageQuests(); //start QuestController

        } else if(choice.equals("4")){
            manageArtefacts(); //start ArtefactController

        } else if(choice.equals("5")){
            showStudentInfo(); //summary of student wallet, bought artefacts

        } else if(choice.equals("6")){
            markStudentArtefacts();

        } else if(choice.equals("7")){
            markStudentQuest();

        } else if(choice.equals("0")){
            System.exit(0);
        }
    }

    public static void getNewStudentInfo(){

        String name = MentorView.typeStudentName();
        String surname = MentorView.typeStudentSurname();
        String password = MentorView.typeStudentPassword();
        String mail = MentorView.typeStudentMail();
        String status = "student";

        CourseController.listAllCourses();
        Integer id = MentorView.getCourseId();

        createNewStudent(name, surname, password, mail, status, id);
    }

    public static void createNewStudent(String name, String surname, String password,
                                        String mail, String status, Integer id){

        Student student = new Student(name, surname, password, mail, status);
        assignStudentToCourse(student, CourseDAO.createCourseFromDatabase(id));

        saveStudent(student);
    }

    public static void assignStudentToCourse(Student student, Course course){

        student.setCourse(course);
    }

    private static void saveStudent(Student student) {

        DaoStudent myStudent = new DaoStudent(student);
        myStudent.save();
    }

    public static void showStudentInfo(){
        String table = "users";
        String status = "student";
        ArrayList<String> studentsList = new UserDao().listSpecifiedData(table, status);
        for(String student : studentsList){
            System.out.println(student);
        }

    }
    public static void showCourseInfo(){

        CourseController.listAllCourses();
    }

    public static void manageQuests(){

        System.out.println("To be implemented");
    }

    public static void manageArtefacts(){

        System.out.println("To be implemented");
    }

    public static void markStudentArtefacts(){

        System.out.println("To be implemented");
    }

    public static void markStudentQuest(){

        System.out.println("To be implemented");
    }

}
