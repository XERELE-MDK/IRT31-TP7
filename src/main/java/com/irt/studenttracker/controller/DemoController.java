package com.irt.studenttracker.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemoController {

    @GetMapping("/hello")
    public String sayHello(Model theModel){
        theModel.addAttribute("theDate",java.time.LocalDateTime.now());
                return "helloworld";
    }

    @RequestMapping("/processForm")
    public String processForm(HttpServletRequest request, Model model) {
        String name = request.getParameter("studentName");
        model.addAttribute("message", "Hello " + name);
        return "helloworld";
    }
    @RequestMapping("/processFormVersionTwo")
    public String letsShoutDude(HttpServletRequest request, Model model) {
        String name = request.getParameter("studentName").toUpperCase();
        model.addAttribute("message", "Yo! " + name);
        return "helloworld";
    }
    @RequestMapping("/processFormVersionThree")
    public String processFormV3(@RequestParam("studentName") String name, Model model) {
        model.addAttribute("message", "Hey my friend ! " + name.toUpperCase());
        return "helloworld";

    }
    @GetMapping("/showForm")
    public String showForm() {
        return "helloworld";
    }
    @PostMapping("/processForm") // écrase la version /processForm précédente
    public String process(@RequestParam("studentName") String name, Model model) {
        model.addAttribute("message", "Bonjour " + name);
        return "helloworld";
    }

}
