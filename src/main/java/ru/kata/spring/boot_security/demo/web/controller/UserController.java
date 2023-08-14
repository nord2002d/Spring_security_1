package ru.kata.spring.boot_security.demo.web.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNameException;
import ru.kata.spring.boot_security.demo.web.model.Role;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.service.UserService;

import java.security.Principal;
import java.util.Set;


@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public String userDate(Principal principal, ModelMap model) {
        model.addAttribute("user",userService.getUserOnNme(principal.getName()));
        return "user";
    }
    @GetMapping("/registration")
    public String printRegistration(ModelMap model) {
        model.addAttribute("reg", new User());
        return "registration";
    }
    @PostMapping("/registration")
    public String  registrationTest(@RequestParam("username") String username,
                                    @RequestParam("surName") String surName,
                                    @RequestParam("age") int age,
                                    @RequestParam("password") String password,
                                    @RequestParam("roles") String roles) throws UserNameException {
        User user = new User(username, surName, age, new BCryptPasswordEncoder().encode(password), Set.of(Role.valueOf(roles)));
        userService.add(user);
        return "redirect:/";
    }

}