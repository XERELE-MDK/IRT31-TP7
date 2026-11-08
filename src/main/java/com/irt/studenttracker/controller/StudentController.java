package com.irt.studenttracker.controller;



import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.irt.studenttracker.model.Student;
import java.util.List;

@Controller
public class StudentController {

//    @GetMapping("/showStudentForm")
//    public String showForm(Model theModel) {
//        theModel.addAttribute("student", new Student()); // étudiant vide
//        return "student-form";
//    }



    @Value("${countries}")
    private List<String> countries;

    @Value("${languages}")
    private List<String> languages;

    @Value("${systems}")
    private List<String> systems;

    @GetMapping("/showStudentForm")
    public String showForm(Model theModel) {
        theModel.addAttribute("student", new Student());
        theModel.addAttribute("countries", countries);
        theModel.addAttribute("languages", languages);   // Ex. 5.7
        theModel.addAttribute("systems", systems);       // Ex. 5.8
        return "student-form";
    }

    @PostMapping("/processStudentForm")
    public String processForm(@ModelAttribute("student") Student theStudent) {
        System.out.println("theStudent: " + theStudent.getFirstName());
        return "student-confirmation";
    }




}
