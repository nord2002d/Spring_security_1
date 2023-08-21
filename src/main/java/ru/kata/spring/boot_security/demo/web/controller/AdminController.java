package ru.kata.spring.boot_security.demo.web.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNameException;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.web.model.Role;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.service.UserService;

import javax.validation.Valid;
import java.util.Set;

@Validated
@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    private static final String REDIRECT = "redirect:/admin/users";

    @GetMapping
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/users")
    public String printWelcome(ModelMap model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @PostMapping
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("surName") String surName,
                          @RequestParam("age") int age,
                          @RequestParam("password") String password,
                          @RequestParam("roles") String roles) throws UserNameException {
        User user = new User(username, surName, age, new BCryptPasswordEncoder().encode(password), Set.of(Role.valueOf(roles)));
        userService.add(user);
        return REDIRECT;
    }

    @GetMapping("/formAdd")
    public String getFormAddUser(ModelMap model) {
        model.addAttribute("add", new User());
        return "add";
    }

    @DeleteMapping
    public String removeUser(@RequestParam("id") long id) throws UserNotFoundException {
        userService.removeUser(id);
        return REDIRECT;
    }

    @GetMapping("/formUpdate")
    public String getFormUpdate(ModelMap model, @RequestParam("id") long id) throws UserNotFoundException {
        model.addAttribute("update", userService.getUser(id));
        return "update";
    }

    @PatchMapping
    public String updateUsers(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) throws UserNameException {
            userService.update(user);
        return REDIRECT;
    }

}
