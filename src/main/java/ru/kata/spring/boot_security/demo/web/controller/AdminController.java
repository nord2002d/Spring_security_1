package ru.kata.spring.boot_security.demo.web.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNameException;
import ru.kata.spring.boot_security.demo.web.model.Role;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.service.UserService;

import java.util.Set;

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
    public String removeUser(@RequestParam("id") long id) {
        userService.removeUser(id);
        return REDIRECT;
    }

    @GetMapping("/formUpdate")
    public String getFormUpdate(ModelMap model, @RequestParam("id") long id) {
        model.addAttribute("update", userService.getUser(id));
        return "update";
    }

    @PatchMapping
    public String updateUsers(@RequestParam("id") long id,
                              @RequestParam("username") String username,
                              @RequestParam("surName") String surName,
                              @RequestParam("age") int age,
                              @RequestParam("password") String password,
                              @RequestParam("roles") String roles) throws UserNameException {

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setSurName(surName);
        user.setAge(age);
        User user1 = userService.getUser(id);
        if (user1.getPassword().equals(password)) {
            user.setPassword(password);
        } else {
            user.setPassword(new BCryptPasswordEncoder().encode(password));
        }
        user.setRoles(Set.of(Role.valueOf(roles)));
        userService.update(user);
        return REDIRECT;
    }

}
