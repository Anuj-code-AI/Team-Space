package com.example.anuj.TeamManager.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";  // loads index.html from templates
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/auth")
    public String auth(Model model) {
        return "auth";
    }

    @GetMapping("/oauth")
    public String oauth(Model model) {
        return "oauth";
    }

    @GetMapping("/team")
    public String team(Model model){
        return "team";
    }
}
