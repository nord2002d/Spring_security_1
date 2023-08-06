package ru.kata.spring.boot_security.demo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    private static final String REDIRECT = "redirect:/admin";

    @GetMapping("/users")
    public String printWelcome(ModelMap model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }
    @GetMapping(value = "/add")
    public String addPP(ModelMap model) {
        model.addAttribute("add", new User());
        return "add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        User userTest = userService.getUserOnNme(user.getUsername());
        if (userTest != null) {
            return "error_name";
        }
        userService.add(user);
        return REDIRECT;
    }

    @GetMapping(value = "/delete")
    public String remove(ModelMap model) {
        model.addAttribute("delete", new User());
        return "delete";
    }

    @PostMapping("/delete")
    public String removeUser(@ModelAttribute User user) {
        User us = userService.getUser(user.getId());
        if (us.getId() == null) {
            return "redirect:/error";
        }
        userService.removeUser(user.getId());
        return REDIRECT;
    }

    @GetMapping(value = "/update")
    public String update(ModelMap model) {
        model.addAttribute("update", new User());
        return "update";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        User updateUser = userService.getUser(user.getId());
        if (updateUser.getId() == null) {
            return "redirect:/error";
        }
        updateUser.setUsername(user.getUsername());
        updateUser.setSurName(user.getSurName());
        updateUser.setAge(user.getAge());
        userService.add(updateUser);
        return REDIRECT;
    }
}
