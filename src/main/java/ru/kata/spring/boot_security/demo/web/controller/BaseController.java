package ru.kata.spring.boot_security.demo.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.service.UserService;

import java.security.Principal;

@AllArgsConstructor
@Controller
public class BaseController {

    private UserService userService;
    private static final String REDIRECT = "redirect:/admin";

    @GetMapping("/admin/users")
    public String printWelcome(ModelMap model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
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
    public String  registrationTest(@ModelAttribute User user) {
        userService.add(user);
        return "redirect:/";
    }

    @GetMapping(value = "/admin/add")
    public String addPP(ModelMap model) {
        model.addAttribute("add", new User());
        return "add";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute User user) {
        User userTest = userService.getUserOnNme(user.getUsername());
        if (userTest != null) {
            return "error_name";
        }
        userService.add(user);
        return REDIRECT;
    }

    @GetMapping(value = "/admin/delete")
    public String remove(ModelMap model) {
        model.addAttribute("delete", new User());
        return "delete";
    }

    @PostMapping("/admin/delete")
    public String removeUser(@ModelAttribute User user) {
        User us = userService.getUser(user.getId());
        if (us.getId() == null) {
            return "redirect:/error";
        }
        userService.removeUser(user.getId());
        return REDIRECT;
    }

    @GetMapping(value = "/admin/update")
    public String update(ModelMap model) {
        model.addAttribute("update", new User());
        return "update";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute User user) {
        User updateUser = userService.getUser(user.getId());
        if (updateUser.getId() == null) {
            return "redirect:/error";
        }
        updateUser.setName(user.getName());
        updateUser.setSurName(user.getSurName());
        updateUser.setAge(user.getAge());
        userService.add(updateUser);
        return REDIRECT;
    }

}